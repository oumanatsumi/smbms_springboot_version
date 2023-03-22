package com.ouma.service;

import com.ouma.mapper.RoleMapper;
import com.ouma.mapper.UserMapper;
import com.ouma.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleMapper roleMapper;


    @Override
    public List<Role> getRoleList() {
        System.out.println("into roleService");
        return roleMapper.getRoleList();
    }
}
