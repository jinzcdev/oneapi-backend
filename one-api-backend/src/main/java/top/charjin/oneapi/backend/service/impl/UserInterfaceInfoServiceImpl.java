package top.charjin.oneapi.backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import top.charjin.oneapi.backend.exception.BusinessException;
import top.charjin.oneapi.backend.mapper.InterfaceInfoMapper;
import top.charjin.oneapi.backend.mapper.UserInterfaceInfoMapper;
import top.charjin.oneapi.backend.service.UserInterfaceInfoService;
import top.charjin.oneapi.common.model.ErrorCode;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;
import top.charjin.oneapi.common.model.entity.UserInterfaceInfo;
import top.charjin.oneapi.common.model.vo.InterfaceInvokeInfoVo;
import top.charjin.oneapi.common.service.UserInterfaceInvokeService;

import java.util.List;

@DubboService(interfaceClass = UserInterfaceInvokeService.class)
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService, UserInterfaceInvokeService {

    @Autowired
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean isAdd) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (isAdd && userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }

    @Override
    public List<InterfaceInvokeInfoVo> getInterfaceInvokeInfoVoList() {
        return baseMapper.selectInterfaceInvokeInfoVoList();
    }

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<InterfaceInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(InterfaceInfo::getUrl, url)
                .eq(InterfaceInfo::getMethod, method);
        return interfaceInfoMapper.selectOne(lqw);
    }

    @Override
    public Integer getInvokeCount(Long interfaceInfoId, Long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = this.getOne(new LambdaQueryWrapper<UserInterfaceInfo>()
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId));
        if (userInterfaceInfo == null) {
            return 0;
        } else {
            return userInterfaceInfo.getLeftNum();
        }
    }

    @Override
    public boolean hasInvokeCount(Long interfaceInfoId, Long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean reduceUserRemainingInvokeTimes(Long interfaceInfoId, Long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaUpdateWrapper<UserInterfaceInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId);
        wrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(wrapper);
    }
}




