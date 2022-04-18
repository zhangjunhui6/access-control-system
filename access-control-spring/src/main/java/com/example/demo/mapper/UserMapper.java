package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    @Select("select id,name,image from user where flag=0;")
    List<User> getAllUser();

    @Select("select * from user where (id like #{keyword} or name like #{keyword} or image like #{keyword}) and flag=0;")
    List<User> findByKeyWord(String keyword);

    @Insert("insert into user(name,flag) values(#{name},0);")
    int insertUser(String name);

    @Update("update user set flag=#{flag} where id=#{id};")
    int updateFlag(User user);

    @Select("select id from user where id = (select max(id) from user where name=#{name})")
    int getLastIDByName(String name);

    @Update("update user set facefeature=null,image=null where id=#{id};")
    int deleteFace(int id);

}
