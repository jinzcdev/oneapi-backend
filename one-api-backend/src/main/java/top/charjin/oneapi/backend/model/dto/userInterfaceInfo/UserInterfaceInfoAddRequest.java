package top.charjin.oneapi.backend.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 调用用户ID
     */
    private Long userId;

    /**
     * 接口信息ID
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;


}