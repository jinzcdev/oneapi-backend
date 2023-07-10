package top.charjin.oneapi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.charjin.oneapi.common.model.entity.User;


/**
 * 用户数据库操作
 */
public interface UserMapper extends BaseMapper<User> {


    @Select("select count(*) from user where userAccount = #{userAccount}")
    int selectUserCount(@Param(value = "userAccount") String userAccount);

}




