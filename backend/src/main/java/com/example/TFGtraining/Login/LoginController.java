package com.example.TFGtraining.Login;

import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> tryToLogin(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, String> response = new HashMap<>();
        response.put("role", userDetails.getUser().getType());
        response.put("token", userDetails.getUser().getToken());
        return response;
    }
}

