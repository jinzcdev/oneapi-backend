package top.charjin.oneapi.common.model.vo;

import lombok.Data;

/**
 * 接口调用信息VO
 */
@Data
public class InterfaceInvokeInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 接口id
     */
    private Integer id;


    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口调用次数
     */
    private Integer totalNum;
}
