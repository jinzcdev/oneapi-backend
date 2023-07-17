package top.charjin.oneapi.backend.controller;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.charjin.oneapi.backend.annotation.AuthCheck;
import top.charjin.oneapi.backend.common.*;
import top.charjin.oneapi.backend.constant.CommonConstant;
import top.charjin.oneapi.backend.constant.UserConstant;
import top.charjin.oneapi.backend.exception.BusinessException;
import top.charjin.oneapi.backend.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import top.charjin.oneapi.backend.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import top.charjin.oneapi.backend.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import top.charjin.oneapi.backend.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import top.charjin.oneapi.backend.service.InterfaceInfoService;
import top.charjin.oneapi.backend.service.UserService;
import top.charjin.oneapi.clientsdk.client.OneApiClient;
import top.charjin.oneapi.clientsdk.exception.IllegalUrlException;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;
import top.charjin.oneapi.common.model.entity.User;
import top.charjin.oneapi.common.model.enums.InterfaceInfoStatusEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.charjin.oneapi.backend.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @Autowired
    private UserService userService;

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取已发布的 API 列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        String name = interfaceInfoQueryRequest.getName();
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        interfaceInfoQuery.setName(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE)) {
            queryWrapper.eq("status", 1);
        }
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    //endregion

    /**
     * 发布
     *
     * @param idRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/publish")
    public BaseResponse<Boolean> publishApi(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return ResultUtils.success(interfaceInfoService.updateById(interfaceInfo));
    }

    /**
     * 下线
     *
     * @param idRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/disable")
    public BaseResponse<Boolean> disableApi(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        return ResultUtils.success(interfaceInfoService.updateById(interfaceInfo));
    }

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<String> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);

        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfoInvokeRequest.getId();
        String requestParams = interfaceInfoInvokeRequest.getRequestParams();

        // 判断是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }

        User loginUser = userService.getLoginUser(request);
        // 获取用户的 ak 与 sk
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        String url = interfaceInfo.getUrl();
        String requestMethod = interfaceInfo.getMethod();
        OneApiClient apiClient = new OneApiClient(accessKey, secretKey);

        try {
            // todo 此处的 GET 与 POST 应该是枚举类型，避免硬编码
            // 根据接口的请求方式调用对应的方法
            if (requestMethod.equals("GET")) {
                try (HttpResponse response = apiClient.doGetWithEncryptedHeader(url + "?" + requestParams)) {
                    if (response.getStatus() == HttpStatus.HTTP_OK) {
                        return ResultUtils.success(response.body());
                    }
                    return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, response.body());
                }
            }
            if (requestMethod.equals("POST")) {
                try (HttpResponse response = apiClient.doPostWithEncryptedHeader(requestParams, url)) {
                    if (response.getStatus() == HttpStatus.HTTP_OK) {
                        return ResultUtils.success(response.body());
                    }
                    return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, response.body());
                }
            }
        } catch (IllegalUrlException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, e.getMessage());
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求方法有误！");
    }


    @GetMapping("/interfaceNameList")
    public BaseResponse<Map> interfaceNameList() {
        List<InterfaceInfo> list = interfaceInfoService.list();
        Map interfaceNameMap = new HashMap();
        for (InterfaceInfo interfaceInfo : list) {
            String name = interfaceInfo.getName();
            interfaceNameMap.put(interfaceInfo.getName(), interfaceInfo.getName());
        }
        return ResultUtils.success(interfaceNameMap);
    }
}
