package com.example.demo.mapper;

import com.example.demo.entity.Device;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMapper {
    /*获取所有设备信息*/
    @Select("select * from device;")
    List<Device> getAllDevices();

    /* 更新设备的地址信息 */
    @Update("update device set location=#{location} where id=#{id};")
    int updateLocation(Device device);

    /* 根据设备名称查找设备*/
    @Select("select * from device where name=#{name}")
    Device findByName(String name);

    /* 添加设备 */
    @Insert("insert into device(id,name,location,state) " +
            "values(#{id},#{name},#{location},#{state});")
    int insertDevice(Device device);

    /* 删除设备 */
    @Delete("delete from device where id=#{id};")
    int deleteDevice(Device device);

    /* 模糊搜索 */
    @Select("select * from device where id like #{keyword} or name like #{keyword} or location like #{keyword} or state like #{keyword};")
    List<Device> findByKeyWord(String keyword);

    /* 获取所有的设备id */
    @Select("select id from device;")
    List<Integer> getAllDeviceId();
}
