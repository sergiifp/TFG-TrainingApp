package com.example.TFGtraining.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public class AuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer")){
            String token = authHeader.replace("Bearer ","");
            CustomAuthenticationToken auth = new CustomAuthenticationToken(token,null,null);
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthenticationManager(ProviderManager providerManager) {
        this.authenticationManager = providerManager;
    }
}
