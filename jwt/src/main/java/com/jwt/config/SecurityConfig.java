package com.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.service.UserDetailsServiceImpl;
import com.jwt.utility.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
	public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/create").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/getallemployee").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/find/").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/update/").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/delete/").hasAnyRole("ADMIN")

                .anyRequest()
                .authenticated();

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}