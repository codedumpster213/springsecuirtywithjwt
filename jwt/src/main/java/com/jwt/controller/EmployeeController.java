package com.jwt.controller;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jwt.entity.Employee;
import com.jwt.request.JwtRequest;
import com.jwt.service.EmployeeServiceImpl;
import com.jwt.service.UserDetailsServiceImpl;
import com.jwt.utility.JwtUtility;

@RestController
// @RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtUtility jwtUtility;

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getEmployeeUserName(),
                            jwtRequest.getPassword()));

            final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(jwtRequest.getEmployeeUserName());

            final String token = jwtUtility.generateTokenForUser(userDetails);

            return ResponseEntity.ok().header( HttpHeaders.AUTHORIZATION, token).build();
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/employee/save").toUriString());
        return ResponseEntity.created(uri).body(employeeServiceImpl.createEmployee(employee));

    }

    @GetMapping("/getallemployee")
    public ResponseEntity<List<Employee>> readAllEmployee() {
        return ResponseEntity.ok().body(employeeServiceImpl.readAllEmployee());
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<Employee> findEmployeeByUserName(@PathVariable("username") String employeeUserName) {
        return ResponseEntity.ok().body(employeeServiceImpl.findEmployeeByUserName(employeeUserName));
    }

    @PostMapping("/update/{username}")
    public ResponseEntity<Employee> updateEmployeeByUserName(@PathVariable("username") String employeeUserName,
            @RequestBody Employee employee) {
        URI uri = URI
                .create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/employee/update/").toUriString());
        return ResponseEntity.created(uri)
                .body(employeeServiceImpl.updateEmployeeByUserName(employeeUserName, employee));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteEmployeeByUserName(@PathVariable("username") String employeeUserName) {
        employeeServiceImpl.deleteEmployeeByUserName(employeeUserName);
        return ResponseEntity.ok().build();
    }
}
