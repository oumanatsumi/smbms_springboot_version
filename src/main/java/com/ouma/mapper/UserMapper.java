package com.ouma.mapper;

import com.ouma.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from smbms_user")
    public List<User> findAll();

    // 得到要登录的用户
    @Select("select * from smbms_user where userCode=#{userCode}")
    public User getLoginUser(String userCode);
    // 修改当前用户密码
    public int updatePwd(int id,String pwd);
    // 查询用户总数
    public int getUserCount(String userName, int userRole);
    // 查询用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    // 增加用户信息
    public int add(User user);

    // 通过userId删除user
    public int deleteUserById(Integer delId);

    // 修改用户信息
    public int modify(User user);

    // 通过userId查询user
    public User getUserById(String id);

}
