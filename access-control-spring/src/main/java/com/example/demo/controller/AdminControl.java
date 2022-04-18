package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.entity.AdminDevice;
import com.example.demo.result.Result;
import com.example.demo.service.AdminDeviceService;
import com.example.demo.service.AdminService;
import com.example.demo.service.DeviceService;
import com.example.demo.utils.ResultUtil;
import com.example.demo.view.AdminView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminControl {
    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminDeviceService adminDeviceService;

    @Autowired
    private DeviceService deviceService;

    /**
     * @Description: 登录功能
     * @Param: [admin]
     * @return: com.example.demo.result.Result
     * @Author: Zjh
     * @Date: 5/19/21
     */
    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestBody Admin admin) {
        Admin admin1 = adminService.Login(admin);
        if (null != admin1) {
            List<Integer> devices;
            if (admin.getUsername().equals("admin")) {
                devices = deviceService.getAllDeviceId();
            } else {
                devices = adminDeviceService.getDidByUsername(admin1.getUsername());
            }

            return ResultUtil.buildSuccessResult("登录成功", devices);
        } else {
            return ResultUtil.buildFailResult("用户名或密码错误", null);
        }
    }

    @GetMapping("/getAllAdmin")
    public List<AdminView> getAllAdmins() {
        List<AdminView> list = new ArrayList<>();
        List<Admin> admins = adminService.getAllAdmin();
        for (Admin admin : admins) {
            List<Integer> devices = adminDeviceService.getDidByUsername(admin.getUsername());
            AdminView userView = new AdminView(admin.getUsername(), admin.getPassword(), devices);
            list.add(userView);
        }
        return list;
    }

    @GetMapping("/findByKeyword")
    public List<AdminView> findByKeyWord(@PathParam("keyword") String keyword) {
        keyword = "%" + keyword + "%";
        List<AdminView> list = new ArrayList<>();
        List<Admin> admins = adminService.findByKeyWord(keyword);
        for (Admin admin : admins) {
            List<Integer> devices = adminDeviceService.getDidByUsername(admin.getUsername());
            AdminView userView = new AdminView(admin.getUsername(), admin.getPassword(), devices);
            list.add(userView);
        }
        return list;
    }

    @RequestMapping("/addAdmin")
    @ResponseBody
    public Result addUser(@RequestBody AdminView adminView) {
        Admin admin = new Admin(adminView.getUsername(), "123456");
        //判断用户名是否已存在
        Admin admin1 = adminService.findByUsername(admin);
        if (null == admin1) {
            //1.新增一个admin,成功则返回1
            int ret = adminService.insertAdmin(admin);
            if (ret == 1) {
                //2.新增权限
                for (int i : adminView.getDevices()) {
                    AdminDevice adminDevice = new AdminDevice(0, adminView.getUsername(), i);
                    int flag = adminDeviceService.insertAdminDevice(adminDevice);
                    if (flag == 0) {
                        return ResultUtil.buildFailResult("增加管理员权限失败", null);
                    }
                }
                return ResultUtil.buildSuccessResult("新增管理员成功,默认密码为123456", null);
            } else {
                return ResultUtil.buildFailResult("增加管理员失败", null);
            }
        } else {
            return ResultUtil.buildFailResult("昵称已存在", null);
        }
    }

    @RequestMapping("/deleteAdmin")
    @ResponseBody
    public Result deleteUser(@RequestBody Admin admin) {
        int flag = adminDeviceService.deleteAllAdminDevice(admin.getUsername());
        if (flag == 0) {
            return ResultUtil.buildFailResult("删除权限失败", null);
        }
        flag = adminService.deleteAdmin(admin);
        if (flag == 0) {
            return ResultUtil.buildFailResult("删除管理员失败", null);
        }
        return ResultUtil.buildSuccessResult("删除管理员成功!", null);
    }

    /**
     * @Description: 修改某个管理员的权限，括号内为新的权限
     * @Param: [username, devices]
     * @return: com.example.demo.result.Result
     * @Author: Zjh
     * @Date: 5/19/21
     */
    @RequestMapping("/changeAuthority")
    @ResponseBody
    public Result changeAuthority(@RequestBody AdminView adminView) {
        String username = adminView.getUsername();
        List<Integer> devices = adminView.getDevices();
        Admin admin = new Admin(username, "");
        if (null != adminService.findByUsername(admin)) {
            if (devices == null) {
                // 清空
                adminDeviceService.deleteAllAdminDevice(username);
            } else {
                // 求差集，删除一些权限
                List<Integer> old_devices = adminDeviceService.getDidByUsername(username);
                old_devices.removeAll(devices);
                for (int i : old_devices) {
                    AdminDevice adminDevice = new AdminDevice(0, username, i);
                    adminDeviceService.deleteOneAdminDevice(adminDevice);
                }
                //求差集，增加一些权限
                old_devices = adminDeviceService.getDidByUsername(username);
                devices.removeAll(old_devices);
                for (int i : devices) {
                    AdminDevice adminDevice = new AdminDevice(0, username, i);
                    adminDeviceService.insertAdminDevice(adminDevice);
                }
            }
            return ResultUtil.buildSuccessResult("修改权限成功", null);
        } else {
            return ResultUtil.buildFailResult("用户不存在", null);
        }
    }

    @RequestMapping("/changePassword")
    @ResponseBody
    public Result changePassword(@RequestParam String username,@RequestParam String oldPassword, @RequestParam String newPassword) {
        System.out.println(username);
        Admin admin = new Admin(username, oldPassword);
        if (null != adminService.Login(admin)){
            admin.setPassword(newPassword);
            int ret = adminService.updatePassword(admin);
            if (ret == 1){
                return ResultUtil.buildSuccessResult("修改密码成功!",null);
            } else {
                return ResultUtil.buildFailResult("修改密码失败!",null);
            }
        }
        return ResultUtil.buildFailResult("旧密码错误!",null);
    }
}
