package com.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.entity.Employee;
import com.jwt.repository.EmployeeRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String employeeUserName) throws UsernameNotFoundException {
    Employee employee = employeeRepository.findByEmployeeUserName(employeeUserName);
    if(employee == null){
        throw new UsernameNotFoundException("Error: employee not found");
    }


         return new UserDetailsImpl(employee);
    }
    
}
