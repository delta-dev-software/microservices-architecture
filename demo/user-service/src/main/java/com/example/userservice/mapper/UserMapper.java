package com.example.userservice.mapper;

import com.example.userservice.domain.User;
import com.example.userservice.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;



@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", ignore = true) // Password is handled separately
    UserDTO userToUserDTO(User user);

    //@Mapping(target = "id", ignore = true) // ID should be auto-generated
    @Mapping(target = "password", ignore = true) // Password is handled separately
    @Mapping(target = "roles", ignore = true) // Map roles if necessary
    User userDTOToUser(UserDTO userDTO);
}
