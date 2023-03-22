package com.ouma.mapper;

import com.ouma.pojo.User;
import org.apache.ibatis.annotations.*;

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
    @Update("update smbms_user set userPassword= #{pwd} where id = #{id}")
    public int updatePwd(int id,String pwd);
    // 查询用户总数
    @Select({"<script>",
            "select count(1) as count from smbms_user u,smbms_role r",
            "<where>",
            "u.userRole = r.id",
            "<if test='userName != null and userName != \"\" '>",
            " and u.userName like '%${userName}%' ",
            "</if>",
            "<if test='userRole != null and userRole != 0 '>",
            " and u.userRole = #{userRole}",
            "</if>",
            "</where>",
            "</script>"})
    public int getUserCount(String userName, int userRole);
    // 查询用户列表

    @Select({"<script>",
            "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r ",
            "<where>",
            "u.userRole = r.id",
            "<if test='userName != null and userName != \"\" '>",
            " and u.userName like '%${userName}%'",
            "</if>",
            "<if test='userRole != null and userRole != 0 '>",
            " and u.userRole = #{userRole}",
            "</if>",
            "</where>",
            " order by creationDate DESC limit #{currentPageNo},#{pageSize}",
            "</script>"})
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    // 增加用户信息
    @Insert("insert into smbms_user (userCode,userName,userPassword,userRole,gender," +
            "birthday,phone,address,creationDate,createdBy) values(#{userCode},#{userName},#{userPassword}," +
            "#{userRole},#{gender},#{birthday},#{phone},#{address},#{creationDate},#{createdBy})")
    public int add(User user);

    // 通过userId删除user
    @Delete("delete from smbms_user where id=#{delId}")
    public int deleteUserById(Integer delId);

    // 修改用户信息
    @Update("update smbms_user set userName=#{userName},gender=#{gender},birthday=#{birthday},phone=#{phone},address=#{address}," +
            "userRole=#{userRole},modifyBy=#{modifyBy},modifyDate=#{modifyDate} where id = #{id} ")
    public int modify(User user);

    // 通过userId查询user
    @Select("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=#{id} and u.userRole = r.id")
    public User getUserById(String id);

}
