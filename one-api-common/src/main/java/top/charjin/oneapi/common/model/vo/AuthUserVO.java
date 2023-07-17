package top.charjin.oneapi.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户鉴权模型
 */
@Data
public class AuthUserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户 ID
     */
    private Long id;
    /**
     * 用户密钥
     */
    private String secretKey;
}