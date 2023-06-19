package com.example.TFGtraining.DTO;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeOfTraining")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RunningSessionDTO.class, name = "runningSession"),
        @JsonSubTypes.Type(value = WeightSessionDTO.class, name = "weightSession")
})
public abstract class TrainingSessionDTO {
    private Long id;
    private LocalTime time;
    private String description;

    public TrainingSessionDTO(Long id, String description, LocalTime time) {
        this.id = id;
        this.description = description;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
