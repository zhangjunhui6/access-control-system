package com.example.demo.mapper;

import com.example.demo.entity.UserDevice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceMapper {
    @Select("select distinct did from user_device where did=#{did};")
    int isDeviceExist(int did);

    @Select("select distinct did from user_device where uid=#{uid};")
    List<Integer> findDidByUid(int uid);

    @Insert("insert into user_device(uid,did) values(#{uid},#{did});")
    int insertUserDevice(UserDevice userDevice);

    @Delete("delete from user_device where uid=#{uid} and did=#{did};")
    int deleteOneUserDevice(UserDevice userDevice);

    @Delete("delete from user_device where uid=#{uid};")
    int deleteAllUserDevice(int uid);

}
