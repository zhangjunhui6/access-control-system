package com.example.demo.service;


import com.example.demo.entity.AdminDevice;
import com.example.demo.mapper.AdminDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDeviceService {
    @Autowired
    private AdminDeviceMapper adminDeviceMapper;

    public int isDeviceExist(int did) {
        return adminDeviceMapper.isDeviceExist(did);
    }

    public List<Integer> getDidByUsername(String username) {
        return adminDeviceMapper.getDidByUsername(username);
    }

    public int insertAdminDevice(AdminDevice adminDevice) {
        return adminDeviceMapper.insertAdminDevice(adminDevice);
    }

    public int deleteOneAdminDevice(AdminDevice adminDevice) {
        return adminDeviceMapper.deleteOneAdminDevice(adminDevice);
    }

    public int deleteAllAdminDevice(String username) {
        return adminDeviceMapper.deleteAllAdminDevice(username);
    }
}
