package top.charjin.oneapi.common.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接口信息视图
 */
@Data
public class InterfaceInfoVO implements Serializable {


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 请求参数
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

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人信息
     */
    private UserVO user;

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 是否被当前用户所拥有
     */
    private Boolean isOwnerByCurrentUser;


    /**
     * 包装类转对象
     *
     * @param interfaceInfoVO
     * @return
     */
    public static InterfaceInfo voToObj(InterfaceInfoVO interfaceInfoVO) {
        if (interfaceInfoVO == null) {
            return null;
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoVO, interfaceInfo);
        return interfaceInfo;
    }

    /**
     * 对象转包装类
     *
     * @param interfaceInfo
     * @return
     */
    public static InterfaceInfoVO objToVo(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        return interfaceInfoVO;
    }
}
