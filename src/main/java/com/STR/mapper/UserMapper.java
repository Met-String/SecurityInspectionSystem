package com.STR.mapper;

import com.STR.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User findUserByPhoneNumber(String phoneNumber);
    User findUserBySiteID(int site_id);
    int addNewUser(User user);
    List<User> findAllUsers();

}
//    List<Area> findAllArea(Area area);
//    int deleteArea(Integer areaId);
//    int addArea(Area area);
//    int count(Area area);