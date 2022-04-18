package com.example.demo.controller;

import com.example.demo.entity.Device;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDevice;
import com.example.demo.result.Result;
import com.example.demo.service.UserDeviceService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ResultUtil;
import com.example.demo.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: access_control
 * @description: User Manager
 * @author: Zjh
 * @create: 2021-05-20 00:54
 **/
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserControl {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDeviceService userDeviceService;

    @GetMapping("/getAllUser")
    public List<UserView> getAllUsers() {
        List<UserView> list = new ArrayList<>();
        List<User> users = userService.getAllUser();
        for (User user : users) {
            List<Integer> devices = userDeviceService.findDidByUid(user.getId());
            UserView userView = new UserView(user.getId(), user.getName(), user.getImage(), devices);
            list.add(userView);
        }
        return list;
    }

    @GetMapping("/findByKeyword")
    public List<UserView> findByKeyWord(@PathParam("keyword") String keyword) {
        keyword = "%" + keyword + "%";
        List<UserView> list = new ArrayList<>();
        List<User> users = userService.findByKeyWord(keyword);
        for (User user : users) {
            List<Integer> devices = userDeviceService.findDidByUid(user.getId());
            UserView userView = new UserView(user.getId(), user.getName(), user.getImage(), devices);
            list.add(userView);
        }
        return list;
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public Result addUser(@RequestBody UserView userView) {
        String name = userView.getName();
        int flag = userService.insertUser(name);
        if (flag == 0) {
            return ResultUtil.buildFailResult("添加用户失败!", null);
        }
        int uid = userService.getLastIDByName(name);
        List<Integer> devices = userView.getDevices();
        for (int did : devices) {
            UserDevice userDevice = new UserDevice(0, uid, did);
            flag = userDeviceService.insertUserDevice(userDevice);
            if (flag == 0) {
                return ResultUtil.buildFailResult("添加用户权限" + did + "失败!", null);
            }
        }
        return ResultUtil.buildSuccessResult("添加用户以及其权限成功!", null);
    }

    @RequestMapping("/changeAuthority")
    @ResponseBody
    public Result changeAuthority(@RequestBody UserView userView) {
        int uid = userView.getId();
        List<Integer> devices = userView.getDevices();
        if (devices.isEmpty()) {
            userDeviceService.deleteAllUserDevice(uid);
            return ResultUtil.buildSuccessResult("修改权限成功!", null);
        } else {
            // 求差集，删除一些数据
            List<Integer> old_devices = userDeviceService.findDidByUid(uid);
            old_devices.removeAll(devices);
            for (int did : old_devices) {
                UserDevice userDevice = new UserDevice(0, uid, did);
                userDeviceService.deleteOneUserDevice(userDevice);
            }
            // 求差集，增加权限
            old_devices = userDeviceService.findDidByUid(uid);
            devices.removeAll(old_devices);
            for (int did : devices) {
                UserDevice userDevice = new UserDevice(0, uid, did);
                userDeviceService.insertUserDevice(userDevice);
            }
            return ResultUtil.buildSuccessResult("修改权限成功!", null);
        }
    }


    @RequestMapping("/deleteUser")
    @ResponseBody
    public Result deleteUser(@RequestBody User user) {
        int flag = userDeviceService.deleteAllUserDevice(user.getId());
        if (flag == 0) {
            return ResultUtil.buildFailResult("删除权限失败", null);
        }
        user.setFlag(1);
        flag = userService.updateFlag(user);
        if (flag == 0) {
            return ResultUtil.buildFailResult("注销用户失败", null);
        }

        return ResultUtil.buildSuccessResult("注销成功!", null);
    }

    @GetMapping("/deleteFace")
    public Result deleteFace(@PathParam("id") int id){
        int flag = userService.deleteFace(id);
        if (flag == 0){
            return ResultUtil.buildFailResult("删除人脸失败,数据库删除失败",null);
        }
        // 删除本地文件内容
        String fileUrl = "/home/ketty/AscendProjects/access_control_system/presenterserver/facial_recognition/ui/static/face_images";
        fileUrl += id + ".jpg";
        File file = new File(fileUrl);
        if (file.delete()){
            System.out.println("删除文件"+fileUrl+"成功!");
        } else {
            System.out.println("删除文件"+fileUrl+"失败!");
        }
        return ResultUtil.buildSuccessResult("删除人脸成功",null);
    }
}
