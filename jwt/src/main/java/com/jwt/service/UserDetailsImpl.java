package com.jwt.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jwt.entity.Employee;

public class UserDetailsImpl implements UserDetails{


    private Employee employee;


    UserDetailsImpl(Employee employee){
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return List.of(new SimpleGrantedAuthority(employee.getRoles()));
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmployeeUserName();
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
