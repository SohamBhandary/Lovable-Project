package com.Soham.Common_Lib.Security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;

@AutoConfiguration
public class SharedSecurityAutoConfiguration {

    @Bean
    public AuthUtil authUtil() {
        return new AuthUtil();
    }

    @Bean
    public JWTAuthFilter jwtAuthFilter(AuthUtil authUtil, HandlerExceptionResolver handlerExceptionResolver) {
        return new JWTAuthFilter(authUtil, handlerExceptionResolver);
    }
}
