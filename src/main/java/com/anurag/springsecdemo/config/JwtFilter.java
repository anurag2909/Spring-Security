package com.anurag.springsecdemo.config;

import com.anurag.springsecdemo.service.JwtService;
import com.anurag.springsecdemo.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter { // Means this filter will be called on every request

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // Header wiil have a lot of data, we just want authorization header
        String token = null;
        String userName = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){ // may be the client is not sending the bearer token, we need to check
            token = authHeader.substring(7); // We don't want the entire string. It actually has Bearer (6 letter word)<Space>Token
            userName = jwtService.extractUserName(token); // extract username from the token
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null){ // If there is already an authentication object avaialble (means already authenticated)

            // After validation. It should generate the Authentication object.
            // Validate the token
            // Generate the Authentication object

            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // set the authentication in SecurityContextHolder. Earlier, It was not there that's why we are doing this
            }
        }
        filterChain.doFilter(request, response); // To forward to the next filter. We are continuing the filter chain
    }



}
