package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Records {
    private int id;
    private int uid;
    private int did;
    private float temp;
    private String time;
}
