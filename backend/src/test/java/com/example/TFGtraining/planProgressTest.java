package com.example.TFGtraining;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.PlanProgress.PlanProgress;
import com.example.TFGtraining.PlanProgress.PlanProgressRepository;
import com.example.TFGtraining.PlanProgress.PlanProgressService;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.SessionResult.SessionResultService;
import com.example.TFGtraining.Trainer.Trainer;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingPlan.TrainingPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
public class planProgressTest {
    @TestConfiguration
    static class planProgressTestConfiguration {
        @Bean
        public PlanProgressService planProgressService(){
            return new PlanProgressService();
        };
    }

    @Autowired
    private PlanProgressService planProgressService;
    @MockBean
    private PlanProgressRepository planProgressRepository;
    @MockBean
    private TrainingPlanService trainingPlanService;
    @MockBean
    private SessionResultService sessionResultService;

    @BeforeEach
    public void setUp(){
        Athlete user = new Athlete();
        user.setUsername("testUser");
        UsernamePasswordAuthenticationToken returnToken = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user),null,null);
        SecurityContextHolder.getContext().setAuthentication(returnToken);
    }

    @Test
    public void createPlanProgressTrainer(){
        Trainer trainer = new Trainer();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.setUser(trainer);

        try{
            planProgressService.createPlanProgress(1L);
            fail("Exception not found");
        }
        catch(ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }

    @Test
    public void createPlanProgressAlreadyEnrolled(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setId(1L);
        PlanProgress planProgress = new PlanProgress();
        planProgress.setPlan(trainingPlan);
        planProgress.setAthlete((Athlete) userDetails.getUser());
        Mockito.when(planProgressRepository.findByAthleteUsernameAndPlanId(userDetails.getUsername(),trainingPlan.getId())).thenReturn(planProgress);
        try{
            planProgressService.createPlanProgress(1L);
            fail("Exception not found");
        }
        catch(ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.CONFLICT);
        }
    }

    @Test
    public void createPlanProgressNotEnrolledClub(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setId(1L);
        Club club = new Club();
        trainingPlan.setOwnerClub(club);

        Mockito.when(planProgressRepository.findByAthleteUsernameAndPlanId(userDetails.getUsername(),trainingPlan.getId())).thenReturn(null);
        Mockito.when(trainingPlanService.getTrainingPlanById(1L)).thenReturn(trainingPlan);
        try {
            planProgressService.createPlanProgress(1L);
            fail("Exception not found");
        }
        catch (ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }

    @Test
    public void createPlanProgress(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setId(1L);
        Mockito.when(planProgressRepository.findByAthleteUsernameAndPlanId(userDetails.getUsername(),trainingPlan.getId())).thenReturn(null);
        Mockito.when(trainingPlanService.getTrainingPlanById(1L)).thenReturn(trainingPlan);
        planProgressService.createPlanProgress(1L);
    }


}
