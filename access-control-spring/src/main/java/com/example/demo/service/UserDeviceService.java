package com.example.demo.service;

import com.example.demo.entity.UserDevice;
import com.example.demo.mapper.UserDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: access_control
 * @description: user can pass's device
 * @author: Zjh
 * @create: 2021-05-19 21:00
 **/
@Service
public class UserDeviceService {
    @Autowired
    private UserDeviceMapper userDeviceMapper;

    public int isDeviceExist(int did) {
        return userDeviceMapper.isDeviceExist(did);
    }

    public List<Integer> findDidByUid(int uid) {
        return userDeviceMapper.findDidByUid(uid);
    }

    public int insertUserDevice(UserDevice userDevice) {
        return userDeviceMapper.insertUserDevice(userDevice);
    }

    public int deleteOneUserDevice(UserDevice userDevice) {
        return userDeviceMapper.deleteOneUserDevice(userDevice);
    }

    public int deleteAllUserDevice(int uid) {
        return userDeviceMapper.deleteAllUserDevice(uid);
    }
}
