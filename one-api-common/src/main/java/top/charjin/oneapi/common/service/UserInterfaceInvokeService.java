package top.charjin.oneapi.common.service;

import top.charjin.oneapi.common.model.entity.InterfaceInfo;

public interface UserInterfaceInvokeService {


    /**
     * 根据 url 和 method 获取接口信息对象
     *
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

    /**
     * 获取用户对某接口的调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    Integer getInvokeCount(Long interfaceInfoId, Long userId);

    /**
     * 判断是否存在剩余调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean hasInvokeCount(Long interfaceInfoId, Long userId);

    /**
     * 记录一次接口调用（将用户的剩余调用次数减一）
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean reduceUserRemainingInvokeTimes(Long interfaceInfoId, Long userId);
}
