package top.charjin.oneapi.backend.model.vo;

import lombok.Data;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;

/**
 * @ClassName InterfaceVo
 * @Description TODO
 * @Author lish
 * @Date 2023/4/27 9:32
 */
@Data
public class InterfaceVo extends InterfaceInfo {

    private static final long serialVersionUID = 1L;
    /*调用次数*/
    private Integer totalNum;
}
