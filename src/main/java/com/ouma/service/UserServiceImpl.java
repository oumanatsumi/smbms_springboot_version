package com.ouma.service;

import com.ouma.mapper.UserMapper;
import com.ouma.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAllUser() {
        return userMapper.findAll();
    }

    @Override
    public User login(String userCode, String userPassword) {
        User loginUser = userMapper.getLoginUser(userCode);
        System.out.println("Service层");
        System.out.println(loginUser.toString());
        // 匹配密码
        if(null != loginUser){
            if(!loginUser.getUserPassword().equals(userPassword))
                loginUser = null;
        }

        return loginUser;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        boolean flag = false;
        if(userMapper.updatePwd(id,pwd) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public int getUserCount(String queryUserName, int queryUserRole) {
        int res = userMapper.getUserCount(queryUserName,queryUserRole);
        return res;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        List<User> userList = new ArrayList<>();
        return userMapper.getUserList(queryUserName,queryUserRole,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public boolean addUser(User user) {
        boolean flag = false;
        if(userMapper.add(user) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        boolean flag = false;
        if(userMapper.deleteUserById(delId) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean modify(User user) {
        boolean flag = false;
        if(userMapper.modify(user) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User selectUserCodeExist(String userCode) {
        return userMapper.getLoginUser(userCode);
    }

}
