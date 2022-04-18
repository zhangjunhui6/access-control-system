package com.example.demo.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: access_control
 * @description: records
 * @author: Zjh
 * @create: 2021-05-20 16:49
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordView {
    private int id;
    private int uid;
    private String userName;
    private int did;
    private String deviceName;
    private float temp;
    private String time;
}
