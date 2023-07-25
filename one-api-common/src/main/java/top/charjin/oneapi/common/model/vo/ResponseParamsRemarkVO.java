package top.charjin.oneapi.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应参数说明VO
 */
@Data
public class ResponseParamsRemarkVO implements Serializable {
    private static final long serialVersionUID = 3673526667928077840L;
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 说明
     */
    private String remark;
}
