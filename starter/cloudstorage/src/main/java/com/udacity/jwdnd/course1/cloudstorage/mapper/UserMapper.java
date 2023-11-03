package com.udacity.jwdnd.course1.cloudstorage.mapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

//    @Delete("DELELE FROM User WHERE id = #{id}")
//    Integer deleteUser (Integer id);
//
//    @Update("UPDATE User SET username = #{username}, email = #{email} , email = #{email} , email = #{email} , email = #{email} " +
//            " WHERE id = #{id}")
//    Integer updateDelivery(String name,  String email, int id);

}
