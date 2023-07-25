package top.charjin.oneapi.backend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 请求参数
     */
    private String requestParams;
}