package com.example.demo.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: access_control
 * @description: view
 * @author: Zjh
 * @create: 2021-05-20 00:57
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserView {
    private int id;
    private String name;
    private String image;
    private List<Integer> devices;
}
