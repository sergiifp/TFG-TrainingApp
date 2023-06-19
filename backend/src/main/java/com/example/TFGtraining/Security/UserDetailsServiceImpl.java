package com.example.TFGtraining.Security;

import com.example.TFGtraining.User.User;
import com.example.TFGtraining.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loadedUser = userRepository.findByUsername(username);
        if (loadedUser == null){
                throw new UsernameNotFoundException("user not found");
        }
        else{
            return new UserDetailsImpl(loadedUser);
        }
    }
}
