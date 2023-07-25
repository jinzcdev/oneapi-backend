package top.charjin.oneapi.common.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送 ID 请求
 *
 * @author Zhichao
 */
@Data
public class IdRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
}