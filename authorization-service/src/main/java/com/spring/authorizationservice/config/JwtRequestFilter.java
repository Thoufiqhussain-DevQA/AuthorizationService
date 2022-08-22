package com.spring.authorizationservice.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.authorizationservice.service.MyUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService service;

    @Autowired
    private JwtUtil jwtUtil;

    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	

    	//retrieving token from header
        final String authorizationHeader = request.getHeader("Authorization");
        
        log.debug("Performing the Authorization");
        
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        	
        	
        	//actual token
            jwt = authorizationHeader.substring(7);
            
            try {
            	
            	username = jwtUtil.extractUsername(jwt);
            } catch(IllegalArgumentException e) {
            	log.debug("Unable to get JWT Token");
            } catch(ExpiredJwtException e) {
            	log.debug("Jwt Token has Expired");
            } catch (MalformedJwtException e) {
            	log.debug("Invalid JWT");
            }  
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	
        	
            UserDetails userDetails = this.service.loadUserByUsername(username);

            if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            	
            	log.debug( "Jwt Token is validated");

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else {
            	log.debug( "Jwt Token is Invalid");
			}
        }
        chain.doFilter(request, response);
    }

}
