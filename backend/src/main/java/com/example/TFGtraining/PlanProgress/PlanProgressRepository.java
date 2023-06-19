package com.example.TFGtraining.PlanProgress;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlanProgressRepository extends CrudRepository<PlanProgress,Long> {
    PlanProgress findByAthleteUsernameAndPlanId(String username, Long trainingPlanId);

    List<PlanProgress> findAllByPlanId(Long trainingPlanId);
}
