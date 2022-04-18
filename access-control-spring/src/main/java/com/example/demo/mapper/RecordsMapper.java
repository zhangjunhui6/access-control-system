package com.example.demo.mapper;

import com.example.demo.entity.Records;
import com.example.demo.view.RecordView;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordsMapper {
    @Select("select distinct did from records where did=#{did};")
    int isDeviceExist(int did);

    @Select("select A.id,A.uid,B.name as userName,A.did,C.name as deviceName,A.temp,A.time from " +
            "(records A join user B on A.uid=B.id) " +
            "join device C on A.did=C.id")
    List<RecordView> getAllRecords();

    @Select({
            "<script>",
            "select",
            "A.id,A.uid,B.name as userName,A.did,C.name as deviceName,A.temp,A.time",
            "from (records A join user B on A.uid=B.id) join device C on A.did=C.id",
            "where A.did in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<RecordView> getRecordsByDevices(@Param("ids") List<Integer> ids);

    @Select({
            "<script>",
            "select",
            "A.id,A.uid,B.name as userName,A.did,C.name as deviceName,A.temp,A.time",
            "from (records A join user B on A.uid=B.id) join device C on A.did=C.id",
            "where A.did in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "and (A.id like #{keyword} or A.uid like #{keyword} or B.name like #{keyword} or " +
                    "A.did like #{keyword} or C.name like #{keyword} or A.temp like #{keyword} or A.time like #{keyword});",
            "</script>"
    })
    List<RecordView> getRecordsByDevicesAndKeyword(@Param("ids") List<Integer> ids, String keyword);

    @Select("select A.id,A.uid,B.name as userName,A.did,C.name as deviceName,A.temp,A.time from " +
            "(records A join user B on A.uid=B.id) " +
            "join device C on A.did=C.id " +
            "where A.id like #{keyword} or A.uid like #{keyword} or B.name like #{keyword} or " +
            "A.did like #{keyword} or C.name like #{keyword} or A.temp like #{keyword} or A.time like #{keyword};")
    List<RecordView> getRecordsByKeyword(String keyword);

    @Delete("delete from records where id=#{id}")
    int deleteRecord(int id);

    @Select("select * from records where did in #{did};")
    List<Records> getRecords(List<Integer> did);
}
