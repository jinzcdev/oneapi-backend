package top.charjin.oneapi.common.service;



import top.charjin.oneapi.common.model.entity.InterfaceInfo;

/**
* @author 李诗豪
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2022-11-28 17:49:19
*/
public interface InnerInterfaceInfoService{


    /**
     * 从数据库查询接口是否存在
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

}
