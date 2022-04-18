package com.example.demo.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: access_control
 * @description: admin view
 * @author: Zjh
 * @create: 2021-05-20 03:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminView {
    private String username;
    private String password;
    private List<Integer> devices;
}
