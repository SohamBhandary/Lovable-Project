package com.Soham.Lovable_Project.Security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JWTUserprincipal(

        Long userId,
        String username,
        List<GrantedAuthority> authorityList
) {
}
