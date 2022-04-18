package com.example.demo.controller;

import com.example.demo.entity.Device;
import com.example.demo.result.Result;
import com.example.demo.service.AdminDeviceService;
import com.example.demo.service.DeviceService;
import com.example.demo.service.RecordsService;
import com.example.demo.service.UserDeviceService;
import com.example.demo.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @program: access_control
 * @description: 设备管理处理
 * @author: Zjh
 * @create: 2021-05-19 15:06
 **/
@RestController
@RequestMapping("/api/device")
@CrossOrigin
public class DeviceControl {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private AdminDeviceService adminDeviceService;

    @Autowired
    private RecordsService recordsService;

    @GetMapping("/getAllDevices")
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/findByKeyword")
    public List<Device> findByKeyWord(@PathParam("keyword") String keyword) {
        keyword = "%" + keyword + "%";
        return deviceService.findByKeyWord(keyword);
    }

    @RequestMapping("/changeLocation")
    @ResponseBody
    public Result changeLocation(@RequestBody Device device) {
        int flag = deviceService.changeLocation(device);
        if (flag == 1) {
            return ResultUtil.buildSuccessResult("修改位置信息成功", null);
        } else {
            return ResultUtil.buildFailResult("修改位置信息失败!", null);
        }
    }

    @RequestMapping("/addDevice")
    @ResponseBody
    public Result addDevice(@RequestBody Device device) {
        int flag = deviceService.addDevice(device);
        if (flag == 1) {
            return ResultUtil.buildSuccessResult("添加设备成功", null);
        } else {
            return ResultUtil.buildFailResult("设备名称已存在!", null);
        }
    }

    @RequestMapping("/deleteDevice")
    @ResponseBody
    public Result deleteDevice(@RequestBody Device device) {
        /*删除设备前需要确保admin_device、user_device以及records表中都没有该device的信息*/
        int did = device.getId();
        if (did == userDeviceService.isDeviceExist(did) || did == adminDeviceService.isDeviceExist(did)
                || did == recordsService.isDeviceExist(did)) {
            return ResultUtil.buildFailResult("删除设备失败!", null);
        }

        int flag = deviceService.deleteDevice(device);
        if (flag == 1) {
            return ResultUtil.buildSuccessResult("删除设备成功", null);
        } else {
            return ResultUtil.buildFailResult("删除设备失败!", null);
        }
    }
}
