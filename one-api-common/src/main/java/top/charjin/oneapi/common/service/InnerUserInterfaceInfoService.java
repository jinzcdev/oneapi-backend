package top.charjin.oneapi.common.service;


/**
 * @author 李诗豪
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
 * @createDate 2022-12-06 09:36:29
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 判断剩余调用次数
     *
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean validLeftNum(Long userId, Long interfaceInfoId);
}
