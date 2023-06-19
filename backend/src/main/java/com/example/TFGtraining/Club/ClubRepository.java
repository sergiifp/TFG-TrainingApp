package com.example.TFGtraining.Club;

import org.springframework.data.repository.CrudRepository;

public interface ClubRepository extends CrudRepository<Club,Long> {
    Club findByName(String name);
}
