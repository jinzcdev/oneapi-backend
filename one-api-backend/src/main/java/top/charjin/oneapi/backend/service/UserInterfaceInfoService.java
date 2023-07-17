package top.charjin.oneapi.backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.charjin.oneapi.common.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 用户接口信息校验
     *
     * @param userInterfaceInfo
     * @param isAdd
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean isAdd);

}
