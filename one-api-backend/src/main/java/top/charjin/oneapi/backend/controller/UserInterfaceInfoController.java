package top.charjin.oneapi.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.charjin.oneapi.backend.annotation.AuthCheck;
import top.charjin.oneapi.backend.common.BaseResponse;
import top.charjin.oneapi.backend.common.DeleteRequest;
import top.charjin.oneapi.backend.common.ErrorCode;
import top.charjin.oneapi.backend.common.ResultUtils;
import top.charjin.oneapi.backend.constant.CommonConstant;
import top.charjin.oneapi.backend.constant.UserConstant;
import top.charjin.oneapi.backend.exception.BusinessException;
import top.charjin.oneapi.backend.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import top.charjin.oneapi.backend.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import top.charjin.oneapi.backend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import top.charjin.oneapi.backend.model.vo.InterfaceInvokeInfoVo;
import top.charjin.oneapi.backend.service.InterfaceInfoService;
import top.charjin.oneapi.backend.service.UserInterfaceInfoService;
import top.charjin.oneapi.backend.service.UserService;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;
import top.charjin.oneapi.common.model.entity.User;
import top.charjin.oneapi.common.model.entity.UserInterfaceInfo;
import top.charjin.oneapi.common.model.vo.SelfInterfaceDateVo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static top.charjin.oneapi.backend.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Autowired
    private UserInterfaceInfoService userInterfaceInfoService;

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @Autowired
    private UserService userService;


    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(userInterfaceInfoService.removeById(id));
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userInterfaceInfoService.updateById(userInterfaceInfo));
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        if (userInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInfoPage);
    }


    @Transactional
    @PostMapping("/payInterface")
    public BaseResponse<Object> payInterface(String interfaceName, String adminPsd, String payAccount, int num) {

        LambdaQueryWrapper<InterfaceInfo> lqw = new LambdaQueryWrapper<InterfaceInfo>();
        lqw.eq(InterfaceInfo::getName, interfaceName);
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(lqw);

        LambdaQueryWrapper<User> lqw1 = new LambdaQueryWrapper<User>();
        lqw1.eq(User::getUserAccount, payAccount);
        User user = userService.getOne(lqw1);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        LambdaQueryWrapper<UserInterfaceInfo> lqw2 = new LambdaQueryWrapper<UserInterfaceInfo>();
        lqw2.eq(UserInterfaceInfo::getUserId, user.getId());
        lqw2.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfo.getId());
        UserInterfaceInfo one = userInterfaceInfoService.getOne(lqw2);
        if (one != null) {
            one.setLeftNum(one.getLeftNum() + num);
            userInterfaceInfoService.saveOrUpdate(one);
        } else {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(user.getId());
            userInterfaceInfo.setInterfaceInfoId(interfaceInfo.getId());
            userInterfaceInfo.setLeftNum(num);
            userInterfaceInfoService.save(userInterfaceInfo);
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/selfInterfaceData")
    public BaseResponse<List<SelfInterfaceDateVo>> selfInterfaceData(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        Long id = currentUser.getId();
        LambdaQueryWrapper<UserInterfaceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInterfaceInfo::getUserId, id);
        List<UserInterfaceInfo> list = userInterfaceInfoService.list(wrapper);
        List<SelfInterfaceDateVo> selfInterfaceDateVos = new ArrayList<>();
        for (UserInterfaceInfo userInterfaceInfo : list) {
            SelfInterfaceDateVo selfInterfaceDateVo = new SelfInterfaceDateVo();
            BeanUtils.copyProperties(userInterfaceInfo, selfInterfaceDateVo);
            Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
            LambdaQueryWrapper<InterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            interfaceInfoLambdaQueryWrapper.eq(InterfaceInfo::getId, interfaceInfoId);
            InterfaceInfo one = interfaceInfoService.getOne(interfaceInfoLambdaQueryWrapper);
            String name = one.getName();
            selfInterfaceDateVo.setInterfaceName(name);
            selfInterfaceDateVos.add(selfInterfaceDateVo);
        }
        return ResultUtils.success(selfInterfaceDateVos);
    }

    @GetMapping("/statistics")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInvokeInfoVo>> getInterfaceStatistics() {
        List<InterfaceInvokeInfoVo> invokeInfoVoList = this.userInterfaceInfoService.getInterfaceInvokeInfoVoList();
        return ResultUtils.success(invokeInfoVoList);
    }

}
