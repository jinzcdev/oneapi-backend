package top.charjin.oneapi.common.service;



import top.charjin.oneapi.common.model.entity.User;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {

    /**
     * 数据库中查看已分配给用户的密钥
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
