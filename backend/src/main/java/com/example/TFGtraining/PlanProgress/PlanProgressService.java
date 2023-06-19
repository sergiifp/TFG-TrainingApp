package com.example.TFGtraining.PlanProgress;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.DTO.PlanProgressDTO;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.SessionResult.SessionResult;
import com.example.TFGtraining.SessionResult.SessionResultService;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingPlan.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanProgressService {
    @Autowired
    private PlanProgressRepository planProgressRepository;

    @Autowired
    private TrainingPlanService trainingPlanService;

    @Autowired
    private SessionResultService sessionResultService;

    public void createPlanProgress(Long trainingPlanId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        Athlete athlete = (Athlete) userDetails.getUser();

        PlanProgress planProgress = planProgressRepository.findByAthleteUsernameAndPlanId(userDetails.getUsername(),trainingPlanId);
        if (planProgress != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You are already enrolled to this plan");
        }
        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanById(trainingPlanId);
        if (trainingPlan.getOwnerClub() != null && !trainingPlan.getOwnerClub().getEnrolledAthletes().contains(athlete)){
            Athlete athleteEnrolled = trainingPlan.getOwnerClub().getEnrolledAthletes().stream().filter(a->a.getUsername().equals(athlete.getUsername())).findAny().orElse(null);
            if (athleteEnrolled == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This plan belongs to a club and you don't take part of this club");
            }
        }
        planProgress = new PlanProgress(athlete, trainingPlan);
        planProgressRepository.save(planProgress);
    }

    public PlanProgressDTO getPlanProgressByUsernameAndPlanId(String username, Long trainingPlanId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }

        PlanProgress planProgress = planProgressRepository.findByAthleteUsernameAndPlanId(username,trainingPlanId);
        System.out.println(planProgress);
        if (planProgress != null){
            List<SessionResult> list = sessionResultService.getSessionResultsByAthleteAndPlan(userDetails.getUsername(), planProgress.getPlan().getId());
            return new PlanProgressDTO(planProgress, list);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No exist progress plan");
        }

    }

    public void deletePlanProgress(String username, Long trainingPlanId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        PlanProgress planProgress = planProgressRepository.findByAthleteUsernameAndPlanId(username,trainingPlanId);
        if (planProgress == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No exist progress plan");
        }
        else {
            sessionResultService.deleteAllSession(username, trainingPlanId);
            planProgressRepository.delete(planProgress);
        }
    }

    public List<PlanProgressDTO> getPlanProgressByPlanId(Long trainingPlanId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUser().getType().equals("Trainer")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
        }

        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanById(trainingPlanId);
        if (trainingPlan.getOwnerClub() == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This plan does not belong to a club");
        }
        if (!trainingPlan.getOwnerClub().getTrainerCreator().getUsername().equals(userDetails.getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this plan");
        }
        List<PlanProgress> planProgressList = planProgressRepository.findAllByPlanId(trainingPlanId);
        List<PlanProgressDTO> dtoList = new ArrayList<>();
        for (PlanProgress plan : planProgressList){
            List<SessionResult> list = sessionResultService.getSessionResultsByAthleteAndPlan(plan.getAthlete().getUsername(), plan.getPlan().getId());
            dtoList.add(new PlanProgressDTO(plan, list));
        }
        return dtoList;
    }
}
