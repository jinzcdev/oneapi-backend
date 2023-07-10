package top.charjin.oneapi.backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;

/**
 * @author Zhichao
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
