package com.jwt.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmployeeUserName(String employeeUserName);
   @Transactional
    void deleteByEmployeeUserName(String employeeUserName);
}
