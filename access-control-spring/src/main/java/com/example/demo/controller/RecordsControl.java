package com.example.demo.controller;

import com.example.demo.entity.Records;
import com.example.demo.result.Result;
import com.example.demo.service.RecordsService;
import com.example.demo.utils.ResultUtil;
import com.example.demo.view.RecordView;
import com.example.demo.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @program: access_control
 * @description: records control
 * @author: Zjh
 * @create: 2021-05-20 16:51
 **/
@RestController
@RequestMapping("/api/record")
@CrossOrigin
public class RecordsControl {

    @Autowired
    private RecordsService recordsService;

    @GetMapping("/getAllRecords")
    public List<RecordView> getAllRecords() {
        return recordsService.getAllRecords();
    }

    @GetMapping("/findByKeyword")
    public List<RecordView> findByKeyWord(@PathParam("keyword") String keyword) {
        keyword = "%" + keyword + "%";
        return recordsService.getRecordsByKeyword(keyword);
    }

    @RequestMapping("/getRecords")
    @ResponseBody
    public List<RecordView> getRecordsByDevices(@RequestBody UserView userView) {
        return recordsService.getRecordsByDevices(userView.getDevices());
    }

    @RequestMapping("/findByKeyWordAndDevices")
    @ResponseBody
    public List<RecordView> getRecordsByDevicesAndKeyword(@RequestBody UserView userView, @PathParam("keyword") String keyword) {
        keyword = "%" + keyword + "%";
        return recordsService.getRecordsByDevicesAndKeyword(userView.getDevices(), keyword);
    }

    @RequestMapping("/deleteRecord")
    @ResponseBody
    public Result deleteUser(@RequestBody Records records) {
        if (recordsService.deleteRecord(records.getId()) == 0) {
            return ResultUtil.buildFailResult("删除记录失败!", null);
        }
        return ResultUtil.buildSuccessResult("删除记录成功!", null);
    }
}
