package com.example.TFGtraining.DTO;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class ExtendedTrainingPlanDTO {
    private Long id;
    private String name;
    private String typeOfActivity;
    private String objective;
    private String description;


    private BriefTrainerDTO trainer;
    private Long clubId;
    @Valid
    private List<TrainingWeekDTO> trainingWeeks;

    public ExtendedTrainingPlanDTO(Long id, String name, String typeOfActivity, String objective, String description) {
        this.id = id;
        this.name = name;
        this.typeOfActivity = typeOfActivity;
        this.objective = objective;
        this.description = description;
        this.trainingWeeks = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfActivity() {
        return typeOfActivity;
    }

    public void setTypeOfActivity(String typeOfActivity) {
        this.typeOfActivity = typeOfActivity;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TrainingWeekDTO> getTrainingWeeks() {
        return trainingWeeks;
    }

    public void setTrainingWeeks(List<TrainingWeekDTO> trainingWeeks) {
        this.trainingWeeks = trainingWeeks;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public BriefTrainerDTO getTrainer() {
        return trainer;
    }

    public void setTrainer(BriefTrainerDTO trainer) {
        this.trainer = trainer;
    }
}
