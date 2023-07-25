package top.charjin.oneapi.backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.charjin.oneapi.common.model.entity.UserInterfaceInfo;
import top.charjin.oneapi.common.model.vo.InterfaceInvokeInfoVo;

import java.util.List;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 用户接口信息校验
     *
     * @param userInterfaceInfo
     * @param isAdd
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean isAdd);

    List<InterfaceInvokeInfoVo> getInterfaceInvokeInfoVoList();

}
