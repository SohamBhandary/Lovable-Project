package com.Soham.Account_Service.Mapper;

import com.Soham.Account_Service.DTOs.Auth.SignupRequest;
import com.Soham.Account_Service.DTOs.Auth.UserProfileResponse;
import com.Soham.Account_Service.Entities.User;
import com.Soham.Common_Lib.DTOs.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Explicitly mapping the record components to entity fields
    @Mapping(source = "username", target = "username")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    User toEntity(SignupRequest signupRequest);

    UserProfileResponse toUserProfileResponse(User user);

    UserDto toUserDto(User user);
}