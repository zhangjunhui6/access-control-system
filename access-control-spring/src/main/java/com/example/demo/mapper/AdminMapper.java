package com.example.demo.mapper;

import com.example.demo.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    @Select("select * from admin where username=#{username} and password=#{password};")
    Admin login(Admin admin);

    @Select("select username from admin where username<>'admin';")
    List<Admin> getAllAdmin();

    @Select("select username from admin where (username like #{keyword}) and username<>'admin';")
    List<Admin> findByKeyWord(String keyword);

    @Insert("insert into admin(username,password) values(#{username},#{password});")
    int insertAdmin(Admin admin);

    @Update("update admin set password=#{password} where username=#{username};")
    int updatePassword(Admin admin);

    @Delete("delete from admin where username=#{username};")
    int deleteAdmin(Admin admin);

    @Select("select * from admin where username=#{username};")
    Admin findByUsername(Admin admin);
}
