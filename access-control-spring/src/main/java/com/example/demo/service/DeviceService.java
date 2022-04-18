package com.example.demo.service;


import com.example.demo.entity.Device;
import com.example.demo.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    public List<Device> getAllDevices() {
        return deviceMapper.getAllDevices();
    }

    public int changeLocation(Device device) {
        return deviceMapper.updateLocation(device);
    }

    public int addDevice(Device device) {
        if (null != deviceMapper.findByName(device.getName()))
            return 0;
        return deviceMapper.insertDevice(device);
    }

    public int deleteDevice(Device device) {
        return deviceMapper.deleteDevice(device);
    }

    public List<Device> findByKeyWord(String keyword) {
        return deviceMapper.findByKeyWord(keyword);
    }

    public List<Integer> getAllDeviceId() {
        return deviceMapper.getAllDeviceId();
    }
}
