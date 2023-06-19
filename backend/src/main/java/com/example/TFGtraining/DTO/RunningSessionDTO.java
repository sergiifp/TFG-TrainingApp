package com.example.TFGtraining.DTO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RunningSessionDTO extends TrainingSessionDTO  {
    private String type;
    private Integer positiveSlope;
    private Integer negativeSlope;
    private List<RunningSeriesDTO> runningSeries;

    public RunningSessionDTO(Long id, String description, LocalTime time, String type, Integer negativeSlope, Integer positiveSlope) {
        super(id, description, time);
        this.negativeSlope = negativeSlope;
        this.type = type;
        this.positiveSlope = positiveSlope;
        runningSeries = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPositiveSlope() {
        return positiveSlope;
    }

    public void setPositiveSlope(Integer positiveSlope) {
        this.positiveSlope = positiveSlope;
    }

    public Integer getNegativeSlope() {
        return negativeSlope;
    }

    public void setNegativeSlope(Integer negativeSlope) {
        this.negativeSlope = negativeSlope;
    }

    public List<RunningSeriesDTO> getRunningSeries() {
        return runningSeries;
    }

    public void setRunningSeries(List<RunningSeriesDTO> runningSeries) {
        this.runningSeries = runningSeries;
    }
}
