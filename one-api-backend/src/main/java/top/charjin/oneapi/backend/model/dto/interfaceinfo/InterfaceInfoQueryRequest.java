package top.charjin.oneapi.backend.model.dto.interfaceinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.charjin.oneapi.backend.common.PageRequest;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode()
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
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
     * 请求头
     */
    private String requestHeader;
    /**
     * 响应头
     */
    private String responseHeader;
    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 创建人
     */
    private Long userId;
}