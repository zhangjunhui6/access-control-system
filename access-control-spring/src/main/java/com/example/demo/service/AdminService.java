package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public Admin Login(Admin admin) {
        return adminMapper.login(admin);
    }

    public List<Admin> getAllAdmin() {
        return adminMapper.getAllAdmin();
    }

    public List<Admin> findByKeyWord(String keyword) {
        return adminMapper.findByKeyWord(keyword);
    }

    public int insertAdmin(Admin admin) {
        return adminMapper.insertAdmin(admin);
    }

    public int updatePassword(Admin admin) {
        return adminMapper.updatePassword(admin);
    }

    public int deleteAdmin(Admin admin) {
        return adminMapper.deleteAdmin(admin);
    }

    public Admin findByUsername(Admin admin) {
        return adminMapper.findByUsername(admin);
    }
}
