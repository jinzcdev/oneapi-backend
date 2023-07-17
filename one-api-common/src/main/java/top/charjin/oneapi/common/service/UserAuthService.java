package top.charjin.oneapi.common.service;


import top.charjin.oneapi.common.model.vo.AuthUserVO;

/**
 * 用户鉴权服务
 */
public interface UserAuthService {


    /**
     * 根据 accessKey 获取用户认证信息的实体对象
     *
     * @param accessKey
     * @return
     */
    AuthUserVO getAuthUser(String accessKey);
}
