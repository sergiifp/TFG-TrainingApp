package com.example.TFGtraining.DTO;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class TrainingWeekDTO {
    private Long id;
    @NotEmpty(message = "description is mandatory")
    private String description;
    private List<TrainingSessionDTO> trainingSessions;

    public TrainingWeekDTO(Long id, String description) {
        this.id = id;
        this.description = description;
        trainingSessions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TrainingSessionDTO> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSessionDTO> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }
}
