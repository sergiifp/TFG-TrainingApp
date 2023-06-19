package com.example.TFGtraining.DTO;

import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;

public class TrainingPlanDTO {

    private long id;
    private String username;
    private String name;
    private String typeOfActivity;
    private String objective;
    private String description;
    private Integer duration;
    private Integer sessionsPerWeek;
    private Integer numberOfLikes;

    public TrainingPlanDTO(TrainingPlan trainingPlan){
        this.id = trainingPlan.getId();
        this.name = trainingPlan.getName();
        this.typeOfActivity = trainingPlan.getTypeOfActivity();
        this.username = trainingPlan.getTrainer().getUsername();
        this.objective = trainingPlan.getObjective();
        this.description = trainingPlan.getDescription();
        this.duration = trainingPlan.getTrainingWeeks().size();
        Integer med = 0;
        for (TrainingWeek week: trainingPlan.getTrainingWeeks()){
            med += week.getTrainingSessions().size();
        }
        if (med != 0) {
            med = med / trainingPlan.getTrainingWeeks().size();
        }
        this.sessionsPerWeek = med;
        this.numberOfLikes = trainingPlan.getLikedBy().size();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSessionsPerWeek() {
        return sessionsPerWeek;
    }

    public void setSessionsPerWeek(Integer sessionsPerWeek) {
        this.sessionsPerWeek = sessionsPerWeek;
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}