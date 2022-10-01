package com.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.entity.Employee;
import com.jwt.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee createEmployee(Employee employee) {
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeUserName(employee.getEmployeeUserName());
        newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        newEmployee.setName(employee.getName());
        newEmployee.setRoles(employee.getRoles());
        return employeeRepository.save(newEmployee);
    }

    @Override
    public void deleteEmployeeByUserName(String employeeUserName) {
        Employee employee = employeeRepository.findByEmployeeUserName(employeeUserName);
        if(employee != null){
        employeeRepository.deleteByEmployeeUserName(employee.getEmployeeUserName());
        }
        
     }

    @Override
    public Employee findEmployeeByUserName(String employeeUserName) {
      return employeeRepository.findByEmployeeUserName(employeeUserName);
    }

    @Override
    public List<Employee> readAllEmployee() {
         return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployeeByUserName(String employeeUserName,Employee employee) {
    Employee newEmployee = findEmployeeByUserName(employeeUserName);
    newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
    newEmployee.setName(employee.getName());
    newEmployee.setPassword(employee.getPassword());
    newEmployee.setRoles(employee.getRoles());
    
    return employeeRepository.save(newEmployee);
    }
    
}
