package com.example.TFGtraining.TrainingPlan;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPlanRepository extends CrudRepository<TrainingPlan,Long> {
    List<TrainingPlan> findAllByOwnerClubIsNull();
}
