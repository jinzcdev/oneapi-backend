package top.charjin.oneapi.backend.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.charjin.oneapi.common.model.request.PageRequest;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode()
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
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
    /**
     * 0-正常，1-禁用
     */
    private Integer status;
}