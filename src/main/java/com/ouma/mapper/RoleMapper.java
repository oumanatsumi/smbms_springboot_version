package com.ouma.mapper;

import com.ouma.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface RoleMapper {

    // 获取角色列表
    @Select("select * from smbms_role")
    public List<Role> getRoleList();
}
