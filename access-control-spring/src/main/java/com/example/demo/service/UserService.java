package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: access_control
 * @description: User Service
 * @author: Zjh
 * @create: 2021-05-20 00:44
 **/
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    public List<User> findByKeyWord(String keyword) {
        return userMapper.findByKeyWord(keyword);
    }

    public int insertUser(String name) {
        return userMapper.insertUser(name);
    }

    public int updateFlag(User user) {
        return userMapper.updateFlag(user);
    }

    public int getLastIDByName(String name) {
        return userMapper.getLastIDByName(name);
    }

    public int deleteFace(int id){
        return userMapper.deleteFace(id);
    }
}
