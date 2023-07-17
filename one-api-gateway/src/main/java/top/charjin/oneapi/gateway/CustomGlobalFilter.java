package top.charjin.oneapi.gateway;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;
import top.charjin.oneapi.common.model.vo.AuthUserVO;
import top.charjin.oneapi.common.service.UserAuthService;
import top.charjin.oneapi.common.service.UserInterfaceInvokeService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {


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
        String accessKey = headers.getFirst("AccessKey");
        String body = headers.getFirst("Body");
        String timestamp = headers.getFirst("Timestamp");
        String sign = headers.getFirst("Sign");
        String nonce = headers.getFirst("Nonce");


        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L; // 时间戳有效期为 5 分钟
        if (timestamp == null || currentTime - Long.parseLong(timestamp) > FIVE_MINUTES) {
            return handleNoAuth(exchange.getResponse(), HttpStatus.FORBIDDEN, "请求时间戳过期");
        }

        AuthUserVO authUser = userAuthService.getAuthUser(accessKey);
        String secretKey = authUser.getSecretKey();
        String serverSignature = DigestUtil.md5Hex(body + secretKey + nonce + timestamp);

        InterfaceInfo interfaceInfo = userInterfaceInvokeService.getInterfaceInfo(request.getPath().value(), request.getMethodValue());

        // 验证接口是否存在
        if (interfaceInfo == null) {
            return handleNoAuth(exchange.getResponse(), HttpStatus.NOT_FOUND, "接口不存在");
        }

        Long userId = authUser.getId();
        Long interfaceId = interfaceInfo.getId();

        // 验证接口是否还有剩余调用次数
        if (userInterfaceInvokeService.getInvokeCount(interfaceId, userId) <= 0) {
            return handleNoAuth(exchange.getResponse(), HttpStatus.FORBIDDEN, "接口调用次数已用完");
        }

        if (!serverSignature.equals(sign)) {
            return handleNoAuth(exchange.getResponse(), HttpStatus.FORBIDDEN, "请求签名不匹配");
        }
        return handleResponse(exchange, chain, interfaceId, userId);
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceInfoId, Long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓冲区工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                //装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        try {
                                            userInterfaceInvokeService.reduceUserRemainingInvokeTimes(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("用户调用次数减少次数出错！", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer); //释放掉内存
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            log.error("响应code异常：" + getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应错误：" + e.getMessage());
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
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
     * 处理没有权限的请求
     *
     * @param response
     * @return void
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response, HttpStatus status, String message) {
        if (status != HttpStatus.OK) {
            log.info("非法请求: " + message);
            response.setStatusCode(status);
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes())));
    }
}
