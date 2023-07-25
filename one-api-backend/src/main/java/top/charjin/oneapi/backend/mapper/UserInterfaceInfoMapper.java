package top.charjin.oneapi.backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.charjin.oneapi.common.model.entity.UserInterfaceInfo;
import top.charjin.oneapi.common.model.vo.InterfaceInvokeInfoVo;

import java.util.List;


@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    /**
     * 查询接口调用信息
     * 通过接口ID分组，统计每个接口被调用的总次数
     *
     * @return
     */
    List<InterfaceInvokeInfoVo> selectInterfaceInvokeInfoVoList();
}




