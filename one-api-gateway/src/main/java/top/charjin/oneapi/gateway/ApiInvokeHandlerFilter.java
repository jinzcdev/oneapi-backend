package top.charjin.oneapi.gateway;

import cn.hutool.crypto.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;
import top.charjin.oneapi.common.model.vo.AuthUserVO;
import top.charjin.oneapi.common.service.UserAuthService;
import top.charjin.oneapi.common.service.UserInterfaceInvokeService;
import top.charjin.oneapi.gateway.exception.AuthorizationFormatErrorException;
import top.charjin.oneapi.gateway.exception.BusinessException;
import top.charjin.oneapi.gateway.http.AuthorizationParser;
import top.charjin.oneapi.gateway.http.HttpProfile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
@Component
public class ApiInvokeHandlerFilter implements GlobalFilter, Ordered {


    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "localhost");
    @DubboReference
    private UserAuthService userAuthService;
    @DubboReference
    private UserInterfaceInvokeService userInterfaceInvokeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径: " + request.getPath().value());
        log.info("请求方法: " + request.getMethod());
        log.info("请求参数: " + request.getQueryParams());
        String hostName = Objects.requireNonNull(request.getRemoteAddress()).getHostName();
        String ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
        log.info("请求主机名地址: " + hostName);
        log.info("请求IP地址: " + ipAddress);

        if (!checkValidIP(hostName, ipAddress)) {
            log.info("非法请求: 来源地址禁用");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("X-AccessKey");
        String nonce = headers.getFirst("X-Nonce");
        String timestamp = headers.getFirst("X-Timestamp");

        String requestPayload = request.getMethodValue().equals(HttpProfile.REQ_POST) ? request.getBody().toString() : "";

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-AccessKey", accessKey);
        headerMap.put("X-Nonce", nonce);
        headerMap.put("X-Timestamp", timestamp);

        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 2L; // 时间戳有效期为 2 分钟
        if (timestamp == null || currentTime - Long.parseLong(timestamp) > FIVE_MINUTES) {
            throw new BusinessException("请求时间戳过期");
        }

        AuthUserVO authUser = userAuthService.getAuthUser(accessKey);
        if (authUser == null) {
            throw new BusinessException("accessKey 不存在");
        }
        String secretKey = authUser.getSecretKey();


        Map<String, String> params = request.getQueryParams().toSingleValueMap();

        String canonicalQueryString = this.getCanonicalQueryString(params, request.getMethodValue());

        String serverSignature = SignUtil.signParamsSha256(headerMap, canonicalQueryString + requestPayload + secretKey);

        String authorization = headers.getFirst("Authorization");
        if (authorization == null) {
            throw new BusinessException("Authorization 头不存在");
        }
        String sign = "";
        try {
            Map<String, String> authMap = AuthorizationParser.parseByMap(authorization);
            if (authMap.containsKey("Signature")) {
                sign = authMap.get("Signature");
            } else {
                throw new AuthorizationFormatErrorException("Authorization 格式错误");
            }
        } catch (AuthorizationFormatErrorException e) {
            throw new BusinessException(e.getMessage());
        }

        InterfaceInfo interfaceInfo = userInterfaceInvokeService.getInterfaceInfo(request.getPath().value(), request.getMethodValue());

        // 验证接口是否存在
        if (interfaceInfo == null) {
            throw new BusinessException("接口不存在");
        }

        Long userId = authUser.getId();
        Long interfaceId = interfaceInfo.getId();

        if (!serverSignature.equals(sign)) {
            throw new BusinessException("请求签名不匹配");
        }

        // 验证接口是否还有剩余调用次数
        if (userInterfaceInvokeService.getInvokeCount(interfaceId, userId) <= 0) {
            throw new BusinessException("接口调用次数已用完");
        }

        try {
            userInterfaceInvokeService.reduceUserRemainingInvokeTimes(interfaceId, userId);
        } catch (Exception e) {
            log.error("用户调用次数减少次数出错！", e);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 检查IP地址是否合法
     *
     * @param ips
     * @return
     */
    private boolean checkValidIP(String... ips) {
        if (ips == null) {
            return true;
        }
        for (String ip : ips) {
            if (IP_WHITE_LIST.contains(ip)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 生成规范化请求字符串
     *
     * @param params
     * @param method
     * @return
     */
    private String getCanonicalQueryString(Map<String, String> params, String method) {
        if (method != null && method.equals(HttpProfile.REQ_POST)) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String v = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            queryString.append("&").append(entry.getKey()).append("=").append(v);
        }
        if (queryString.length() == 0) {
            return "";
        } else {
            return queryString.substring(1);
        }
    }
}
