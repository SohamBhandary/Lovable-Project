package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Auth.UserProfileResponse;
import com.Soham.Lovable_Project.Services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImple implements UserService {
    //serviceimplemenion becuase fo floosecoupleing betwweting service an d serviceimple
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
