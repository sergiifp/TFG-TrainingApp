package com.example.TFGtraining.Athlete;

import org.springframework.data.repository.CrudRepository;

public interface AthleteRepository extends CrudRepository<Athlete,String> {
    Athlete findByUsername(String username);
}
