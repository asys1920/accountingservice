package com.asys1920.accountingservice.adapter;

import com.asys1920.accountingservice.controller.UserDTO;
import com.asys1920.accountingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDTOtoUser(UserDTO user);

    UserDTO userToUserDTO(User user);
}
