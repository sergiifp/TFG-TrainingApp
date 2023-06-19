package com.example.TFGtraining.SessionResult;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionResultRepository extends CrudRepository<SessionResult,Long> {
    List<SessionResult> findAllByAthlete_username(String username);
}
