package com.Soham.Lovable_Project.Mapper;

import com.Soham.Lovable_Project.DTOs.Auth.SignupRequest;
import com.Soham.Lovable_Project.DTOs.Auth.UserProfileResponse;
import com.Soham.Lovable_Project.Entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User toEntity(SignupRequest signupRequest);
    UserProfileResponse toUserProfileResponse(User user);
}
