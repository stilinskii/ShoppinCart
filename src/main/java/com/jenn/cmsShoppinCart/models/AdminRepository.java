package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
}
