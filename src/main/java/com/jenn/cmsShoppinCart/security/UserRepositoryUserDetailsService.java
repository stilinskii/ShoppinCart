package com.jenn.cmsShoppinCart.security;

import com.jenn.cmsShoppinCart.models.AdminRepository;
import com.jenn.cmsShoppinCart.models.UserRepository;
import com.jenn.cmsShoppinCart.models.data.Admin;
import com.jenn.cmsShoppinCart.models.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        Admin admin = adminRepository.findByUsername(username);

        if(user != null){
            return user;
        }

        if(admin != null){
            return admin;
        }

        throw new UsernameNotFoundException("User: " + username + " not found!");
    }
}
