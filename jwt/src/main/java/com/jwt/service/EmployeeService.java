package com.jwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.entity.Employee;

@Service
public interface EmployeeService {
    
    Employee createEmployee(Employee employee);
    List<Employee> readAllEmployee();
    Employee findEmployeeByUserName(String employeeUserName);
    Employee updateEmployeeByUserName(String employeeUserName,Employee employee);
    void deleteEmployeeByUserName(String employeeUserName);
}
