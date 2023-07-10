package top.charjin.oneapi.backend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 请求头
     */
    private String requestHeader;
    /**
     * 响应头
     */
    private String responseHeader;
    /**
     * 请求类型
     */
    private String method;
}