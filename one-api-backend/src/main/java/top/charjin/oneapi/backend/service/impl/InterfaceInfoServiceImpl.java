package top.charjin.oneapi.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.charjin.oneapi.backend.common.ErrorCode;
import top.charjin.oneapi.backend.exception.BusinessException;
import top.charjin.oneapi.backend.mapper.InterfaceInfoMapper;
import top.charjin.oneapi.backend.service.InterfaceInfoService;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;

/**
 * @author Zhichao
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
//        String description = interfaceInfo.getDescription();
//        String url = interfaceInfo.getUrl();
//        String requestHeader = interfaceInfo.getRequestHeader();
//        String responseHeader = interfaceInfo.getResponseHeader();
//        Integer status = interfaceInfo.getStatus();
//        String method = interfaceInfo.getMethod();
//        Long userId = interfaceInfo.getUserId();
//        Date createTime = interfaceInfo.getCreateTime();
//        Date updateTime = interfaceInfo.getUpdateTime();
//        Integer idDelete = interfaceInfo.getIsDelete();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

    }

}




