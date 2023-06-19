package com.example.TFGtraining.SessionResult;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.PlanProgress.PlanProgress;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingPlan.TrainingPlanService;
import com.example.TFGtraining.TrainingSession.TrainingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionResultService {
    @Autowired
    private SessionResultRepository sessionResultRepository;

    @Autowired
    TrainingPlanService trainingPlanService;

    public void createSessionResult(Long trainingSessionId, Integer finalKm, LocalTime finalTime) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        Athlete athlete = (Athlete) userDetails.getUser();

        TrainingSession trainingSession = trainingPlanService.getTrainingSessionById(trainingSessionId);
        TrainingPlan trainingPlan = trainingSession.getTrainingWeek().getTrainingPlan();
        boolean enrolled = false;
        for (PlanProgress p : trainingPlan.getClientsProgress()){
            if (p.getAthlete().getUsername().equals(athlete.getUsername())){
                enrolled = true;
            }
        }
        if (!enrolled){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not enrolled to this plan");
        }

        SessionResult sessionResult = new SessionResult(athlete, trainingSession, finalKm, finalTime);
        sessionResultRepository.save(sessionResult);
    }

    public Integer getTotalKm(List<SessionResult> results) {
        Integer km = 0;
        for(SessionResult result: results){
            if (result.getFinalKm() != null){
                km += result.getFinalKm();
            }
        }
        return km;
    }

    public List<SessionResult> getSessionResultsByAthleteAndPlan(String username, Long trainingPlanId){
        List<SessionResult> returnList = new ArrayList<>();
        List<SessionResult> list = sessionResultRepository.findAllByAthlete_username(username);
        for (SessionResult s: list){
            if (s.getSession().getTrainingWeek().getTrainingPlan().getId() == trainingPlanId){
                returnList.add(s);
            }
        }
        return returnList;
    }

    public void deleteAllSession(String username, Long trainingPlanId){
        List<SessionResult> returnList = new ArrayList<>();
        List<SessionResult> list = sessionResultRepository.findAllByAthlete_username(username);
        for (SessionResult s: list){
            if (s.getSession().getTrainingWeek().getTrainingPlan().getId() == trainingPlanId){
                returnList.add(s);
            }
        }
        sessionResultRepository.deleteAll(returnList);
    }
}
