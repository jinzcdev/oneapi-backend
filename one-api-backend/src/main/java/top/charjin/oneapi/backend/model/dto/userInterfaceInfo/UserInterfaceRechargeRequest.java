package top.charjin.oneapi.backend.model.dto.userInterfaceInfo;


import lombok.Data;

import java.io.Serializable;


/**
 * 充值请求
 */
@Data
public class UserInterfaceRechargeRequest implements Serializable {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 接口信息ID
     */
    private Long interfaceId;


    /**
     * 充值次数
     */
    private Integer rechargeCount;

}
