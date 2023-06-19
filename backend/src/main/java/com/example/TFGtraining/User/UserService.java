package com.example.TFGtraining.User;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.Trainer.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(Map<String,String> user){
        if (user.get("username") == null || user.get("password") == null || user.get("role") == null || user.get("age") == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All parameters are required");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode(user.get("password"));
        User userEntity;
        if (user.get("role").equals("Athlete")){
            Athlete athlete = new Athlete();
            userEntity = athlete;
        }
        else {
            Trainer trainer = new Trainer();
            userEntity = trainer;
        }
        userEntity.setUsername(user.get("username"));
        userEntity.setToken(passwordEncoder.encode("token_encrypted31"+user.get("username")));
        userEntity.setAge(Integer.valueOf(user.get("age")));
        Boolean exists = userRepository.existsByUsername(user.get("username"));
        if (exists){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this username already exists");
        }
        userEntity.setPassword(passwordEncoded);
        userEntity.setSinceDate(new Date());
        userRepository.save(userEntity);
    }

    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    public String getUserRole(String username) {
        User user = this.userRepository.findByUsername(username);
        return user.getType();
    }

    public User searchUserLogged(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return(userDetails.getUser());
    }

    public User searchUserByToken(String token){
        return(this.userRepository.findByToken(token));
    }

    public void deleteUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepository.deleteById(userDetails.getUsername());
    }
}
