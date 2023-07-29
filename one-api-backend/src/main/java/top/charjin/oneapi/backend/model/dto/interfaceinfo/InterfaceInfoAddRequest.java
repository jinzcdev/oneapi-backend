package top.charjin.oneapi.backend.model.dto.interfaceinfo;


import lombok.Data;
import top.charjin.oneapi.common.model.vo.RequestParamsRemarkVO;
import top.charjin.oneapi.common.model.vo.ResponseParamsRemarkVO;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 主机名
     */
    private String host;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 接口方法名称
     */
    private String action;
    /**
     * 请求参数说明
     */
    private String requestParams;
    /**
     * 请求参数说明
     */
    private List<RequestParamsRemarkVO> requestParamsRemark;
    /**
     * 响应参数说明
     */
    private List<ResponseParamsRemarkVO> responseParamsRemark;
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
}