package com.example.TFGtraining.Mapper;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.DTO.*;
import com.example.TFGtraining.RunningSession.RunningSeries;
import com.example.TFGtraining.RunningSession.RunningSession;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingSession.TrainingSession;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import com.example.TFGtraining.WeightSession.WeightSession;

public class TrainingPlanMapper {
    private static TrainingPlanMapper trainingPlanMapper;

    private TrainingPlanMapper() {
    }

    public static TrainingPlanMapper getInstance(){
        if (trainingPlanMapper == null){
            trainingPlanMapper = new TrainingPlanMapper();
        }
        return trainingPlanMapper;
    }

    public static ExtendedTrainingPlanDTO toDTO(TrainingPlan trainingPlan){
        ExtendedTrainingPlanDTO extendedTrainingPlanDTO = new ExtendedTrainingPlanDTO(trainingPlan.getId(), trainingPlan.getName(), trainingPlan.getTypeOfActivity(), trainingPlan.getObjective(), trainingPlan.getDescription());
        for (TrainingWeek trainingWeek :trainingPlan.getTrainingWeeks()){
            TrainingWeekDTO trainingWeekDTO = new TrainingWeekDTO(trainingWeek.getId(), trainingWeek.getDescription());
            for (TrainingSession trainingSession: trainingWeek.getTrainingSessions()){
                if (trainingSession.getTypeOfSession().equals("Running")){
                    RunningSession runningSession = (RunningSession) trainingSession;
                    RunningSessionDTO runningSessionDTO = new RunningSessionDTO(trainingSession.getId(), trainingSession.getDescription(), trainingSession.getTime(),
                                                                                runningSession.getType(), runningSession.getNegativeSlope(), runningSession.getPositiveSlope());
                    for (RunningSeries runningSeries: runningSession.getRunningSeries()){
                        RunningSeriesDTO runningSeriesDTO = new RunningSeriesDTO(runningSeries.getId(), runningSeries.getDistance(), runningSeries.getReturnDistance(),
                                                                                runningSeries.getRestTime(), runningSeries.getRepetitions(), runningSeries.getFc());
                        runningSessionDTO.getRunningSeries().add(runningSeriesDTO);
                    }
                    trainingWeekDTO.getTrainingSessions().add(runningSessionDTO);
                }
                else{
                    WeightSession weightSession = (WeightSession) trainingSession;
                    WeightSessionDTO weightSessionDTO = new WeightSessionDTO(trainingSession.getId(), trainingSession.getDescription(), trainingSession.getTime(), weightSession.getLoad());
                    trainingWeekDTO.getTrainingSessions().add(weightSessionDTO);
                }
            }
            extendedTrainingPlanDTO.getTrainingWeeks().add(trainingWeekDTO);
        }
        extendedTrainingPlanDTO.setTrainer(new BriefTrainerDTO(trainingPlan.getTrainer().getUsername(), trainingPlan.getTrainer().getAge(), trainingPlan.getTrainer().getSinceDate(), trainingPlan.getTrainer().getTypeOfActivityPlans()));
        Club club = trainingPlan.getOwnerClub();
        if (club != null){
            extendedTrainingPlanDTO.setClubId(club.getId());
        }
        return extendedTrainingPlanDTO;
    }


    public TrainingPlan toEntity(ExtendedTrainingPlanDTO trainingPlanDTO) {
        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanDTO.getId(), trainingPlanDTO.getName(), trainingPlanDTO.getTypeOfActivity(),
                                                trainingPlanDTO.getObjective(), trainingPlanDTO.getDescription());
        for (TrainingWeekDTO weekDTO: trainingPlanDTO.getTrainingWeeks()){
            TrainingWeek trainingWeek = new TrainingWeek(weekDTO.getId(), weekDTO.getDescription());
            trainingWeek.setTrainingPlan(trainingPlan);
            for (TrainingSessionDTO sessionDTO: weekDTO.getTrainingSessions()){
                if (sessionDTO.getClass().isAssignableFrom(RunningSessionDTO.class)){
                    RunningSessionDTO runningSessionDTO = (RunningSessionDTO) sessionDTO;
                    RunningSession runningSession = new RunningSession(runningSessionDTO.getId(), runningSessionDTO.getTime(), runningSessionDTO.getDescription(),
                                                        runningSessionDTO.getType(), runningSessionDTO.getPositiveSlope(), runningSessionDTO.getNegativeSlope());
                    runningSession.setTrainingWeek(trainingWeek);
                    for(RunningSeriesDTO runningSeriesDTO: runningSessionDTO.getRunningSeries()){
                        RunningSeries runningSeries = new RunningSeries(runningSeriesDTO.getId(), runningSeriesDTO.getDistance(), runningSeriesDTO.getReturnDistance(),
                                                                runningSeriesDTO.getRestTime(), runningSeriesDTO.getRepetitions(), runningSeriesDTO.getFc());
                        runningSession.getRunningSeries().add(runningSeries);
                    }
                    trainingWeek.getTrainingSessions().add(runningSession);
                }
                else{
                    WeightSessionDTO weightSessionDTO = (WeightSessionDTO) sessionDTO;
                    WeightSession weightSession = new WeightSession(weightSessionDTO.getId(), weightSessionDTO.getTime(), weightSessionDTO.getDescription(), weightSessionDTO.getLoad());
                    weightSession.setTrainingWeek(trainingWeek);
                    trainingWeek.getTrainingSessions().add(weightSession);
                }
            }
            trainingPlan.getTrainingWeeks().add(trainingWeek);
        }
        return trainingPlan;
    }
}
