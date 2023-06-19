package com.example.TFGtraining.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(){
        this.userService.deleteUser();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody Map<String,String> userInfo) {
        this.userService.saveUser(userInfo);
    }

    @GetMapping("/whoAmI")
    public Map<String,String> whoAmI(){
        User user = this.userService.searchUserLogged();
        Map<String,String> json = new HashMap<>();
        json.put("username", user.getUsername());
        json.put("role", user.getType());
        return json;
    }
}
