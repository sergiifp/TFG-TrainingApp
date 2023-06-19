package com.example.TFGtraining.Security;

import com.example.TFGtraining.User.User;
import com.example.TFGtraining.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        User user;
        try {
            user = userService.searchUserByToken(token);
        } catch (Exception e){
            throw new BadCredentialsException("User with token: "+token+" NOT FOUND");
        }
        UsernamePasswordAuthenticationToken returnToken = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user),null,null);
        return returnToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication
                .equals(CustomAuthenticationToken.class);
    }
}
