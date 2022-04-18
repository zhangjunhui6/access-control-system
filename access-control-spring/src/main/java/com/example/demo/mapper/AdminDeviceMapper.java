package com.example.demo.mapper;

import com.example.demo.entity.AdminDevice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDeviceMapper {
    @Select("select distinct did from admin_device where did=#{did};")
    int isDeviceExist(int did);

    @Select("select did from admin_device where username=#{username};")
    List<Integer> getDidByUsername(String username);

    @Insert("insert into admin_device(username,did) values(#{username},#{did});")
    int insertAdminDevice(AdminDevice adminDevice);

    @Delete("delete from admin_device where username=#{username} and did=#{did};")
    int deleteOneAdminDevice(AdminDevice adminDevice);

    @Delete("delete from admin_device where username=#{username};")
    int deleteAllAdminDevice(String username);


}
