package com.STR.mapper;

import com.STR.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findUserByPhoneNumber(String phoneNumber);
    int addNewUser(User user);
}
//    List<Area> findAllArea(Area area);
//    int deleteArea(Integer areaId);
//    int addArea(Area area);
//    int count(Area area);