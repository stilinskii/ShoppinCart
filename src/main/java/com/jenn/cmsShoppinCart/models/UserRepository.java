package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
}
