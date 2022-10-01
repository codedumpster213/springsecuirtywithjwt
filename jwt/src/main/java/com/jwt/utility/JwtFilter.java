package com.jwt.utility;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.service.UserDetailsServiceImpl;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authorization != null && authorization.startsWith("Bearer ")){
            token  = authorization.substring(7);
            username = jwtUtility.getUsernameFromToken(token);
        }
     

        if (username != null &&  authentication.equals(null)) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

            if (jwtUtility.validateUserToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
               
            }
        }

        filterChain.doFilter(request, response);
    }

}
