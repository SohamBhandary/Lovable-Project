package com.Soham.Account_Service.Mapper;


import com.Soham.Account_Service.DTOs.Auth.SignupRequest;
import com.Soham.Account_Service.DTOs.Auth.UserProfileResponse;
import com.Soham.Account_Service.Entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User toEntity(SignupRequest signupRequest);
    UserProfileResponse toUserProfileResponse(User user);
}
