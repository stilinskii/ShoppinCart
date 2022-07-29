package com.jenn.cmsShoppinCart.models.data;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name="users")
@Data
public class User implements UserDetails {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min=2, message="Username must be at least 2 characters long")
    private String username;

    @Size(min=2, message="Password must be at least 2 characters long")
    private String password;

    //실제 db table에는 없음.
    //이 어노테이션 붙임으로서 not going to persist
    @Transient
    private String confirmPassword;

    @Email(message = "Please enter a valid email")
    private String email;

    @Column(name="phone_number", updatable = false)
    @Size(min=6, message="Phone number must be at least 6 characters long")
    private String phoneNumber;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
