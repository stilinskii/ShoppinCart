package com.jenn.cmsShoppinCart.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Service;

@Service
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //순서가 중요. from allowing less and to allowing more
        http
                .authorizeHttpRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/**").hasAnyRole("USER");//user권한을 가진 사용자만 /이하 endpoint 접근 가능

        //spring EL , more complex
//        http
//                .authorizeHttpRequests()
//                .antMatchers("/category/**").access("hasRole('ROLE_USER')")
//                .antMatchers("/").access("permitAll");

    }


}
