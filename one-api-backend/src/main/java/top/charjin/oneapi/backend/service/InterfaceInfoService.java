package top.charjin.oneapi.backend.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.charjin.oneapi.backend.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import top.charjin.oneapi.backend.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;
import top.charjin.oneapi.common.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zhichao
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);


    /**
     * 获取查询条件
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 获取接口信息封装
     *
     * @param interfaceInfo
     * @param request
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    /**
     * 分页获取接口信息封装
     *
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    /**
     * 修改接口信息
     *
     * @param interfaceInfoUpdateRequest 接口信息修改请求
     * @return 是否成功
     */
    boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest);

    /**
     * 根据用户ID 分页获取接口信息封装
     *
     * @param interfaceInfoPage 接口信息分页
     * @param request           当前会话
     * @return 接口信息分页
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOByUserIdPage(Page<InterfaceInfo> interfaceInfoPage,
                                                         HttpServletRequest request);
}
