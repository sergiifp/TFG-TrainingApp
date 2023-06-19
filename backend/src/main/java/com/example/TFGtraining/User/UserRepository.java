package com.example.TFGtraining.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {
    User findByUsername(String username);

    User findByToken(String token);

    Boolean existsByUsername(String username);
}
