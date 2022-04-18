package com.example.demo.service;

import com.example.demo.entity.Records;
import com.example.demo.mapper.RecordsMapper;
import com.example.demo.view.RecordView;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: access_control
 * @description: records
 * @author: Zjh
 * @create: 2021-05-19 21:01
 **/
@Service
public class RecordsService {
    @Autowired
    private RecordsMapper recordsMapper;

    public int isDeviceExist(int did) {
        return recordsMapper.isDeviceExist(did);
    }

    public List<RecordView> getAllRecords() {
        return recordsMapper.getAllRecords();
    }

    public List<RecordView> getRecordsByKeyword(String keyword) {
        return recordsMapper.getRecordsByKeyword(keyword);
    }

    public List<RecordView> getRecordsByDevices(List<Integer> devices) {
        return recordsMapper.getRecordsByDevices(devices);
    }

    public List<RecordView> getRecordsByDevicesAndKeyword(List<Integer> devices, String keyword) {
        return recordsMapper.getRecordsByDevicesAndKeyword(devices, keyword);
    }

    public int deleteRecord(int id) {
        return recordsMapper.deleteRecord(id);
    }
}
