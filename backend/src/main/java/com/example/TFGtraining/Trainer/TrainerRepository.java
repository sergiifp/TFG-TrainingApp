package com.example.TFGtraining.Trainer;

import org.springframework.data.repository.CrudRepository;

public interface TrainerRepository extends CrudRepository<Trainer,String> {
    Trainer findByUsername(String username);
}
