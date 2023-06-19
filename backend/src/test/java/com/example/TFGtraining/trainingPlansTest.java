package com.example.TFGtraining;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.Club.ClubService;
import com.example.TFGtraining.DTO.*;
import com.example.TFGtraining.RunningSession.RunningSeries;
import com.example.TFGtraining.RunningSession.RunningSeriesRepository;
import com.example.TFGtraining.RunningSession.RunningSession;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.Trainer.Trainer;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import com.example.TFGtraining.TrainingPlan.TrainingPlanRepository;
import com.example.TFGtraining.TrainingPlan.TrainingPlanService;
import com.example.TFGtraining.TrainingSession.TrainingSessionRepository;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import com.example.TFGtraining.TrainingWeek.TrainingWeekRepository;
import com.example.TFGtraining.User.UserService;
import com.example.TFGtraining.WeightSession.WeightSession;
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

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
public class trainingPlansTest {
    @TestConfiguration
    static class trainingPlansTestConfiguration {
        @Bean
        public TrainingPlanService trainingPlanService(){
            return new TrainingPlanService();
        };
    }
    @Autowired
    private TrainingPlanService trainingPlanService;
    @MockBean
    private TrainingPlanRepository trainingPlanRepository;
    @MockBean
    private ClubService clubService;
    @MockBean
    private UserService userService;
    @MockBean
    private TrainingWeekRepository trainingWeekRepository;
    @MockBean
    private TrainingSessionRepository trainingSessionRepository;
    @MockBean
    private RunningSeriesRepository runningSeriesRepository;

    @BeforeEach
    public void setUp(){
        Trainer user = new Trainer();
        user.setUsername("testUser");
        UsernamePasswordAuthenticationToken returnToken = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user),null,null);
        SecurityContextHolder.getContext().setAuthentication(returnToken);
    }

    @Test
    public void ModifyTrainingPlanThatNotExists(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trainer user = new Trainer();
        user.setUsername("testUser");
        TrainingPlan trainingPlan = new TrainingPlan(1L,"plan","road","marathon","description");
        trainingPlan.setTrainer(user);
        ExtendedTrainingPlanDTO extendedTrainingPlanDTO = new ExtendedTrainingPlanDTO(1L,"plan","road","marathon","description");
        Mockito.when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(java.util.Optional.ofNullable(null));
        try {
            trainingPlanService.saveExistingTrainingPlan(extendedTrainingPlanDTO);
            fail("Exception not found");
        }
        catch (ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void ModifyTrainingPlanCreatedByAnother(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trainer user = new Trainer();
        user.setUsername("notLogged");
        TrainingPlan trainingPlan = new TrainingPlan(1L,"plan","road","marathon","description");
        trainingPlan.setTrainer(user);
        ExtendedTrainingPlanDTO extendedTrainingPlanDTO = new ExtendedTrainingPlanDTO(1L,"plan","road","marathon","description");
        Mockito.when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(java.util.Optional.ofNullable(trainingPlan));
        try {
            trainingPlanService.saveExistingTrainingPlan(extendedTrainingPlanDTO);
            fail("Exception not found");
        }
        catch (ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }

    @Test
    public void ModifyTrainingPlanAthlete(){
        Athlete ath = new Athlete();
        ath.setUsername("athleteTest");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.setUser(ath);
        Trainer user = new Trainer();
        user.setUsername("notLogged");
        TrainingPlan trainingPlan = new TrainingPlan(1L,"plan","road","marathon","description");
        trainingPlan.setTrainer(user);
        ExtendedTrainingPlanDTO extendedTrainingPlanDTO = new ExtendedTrainingPlanDTO(1L,"plan","road","marathon","description");
        Mockito.when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(java.util.Optional.ofNullable(trainingPlan));
        try {
            trainingPlanService.saveExistingTrainingPlan(extendedTrainingPlanDTO);
            fail("Exception not found");
        }
        catch (ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }

    @Test
    public void ModifyTrainingPlanAddWeekSessionAndSerie(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Trainer user = (Trainer) userDetails.getUser();

        TrainingPlan trainingPlan = new TrainingPlan(1L,"plan","road","marathon","description");
        trainingPlan.setTrainer(user);
        ExtendedTrainingPlanDTO extendedTrainingPlanDTO = new ExtendedTrainingPlanDTO(1L,"plan","road","marathon","description");
        TrainingWeekDTO trainingWeekDTO = new TrainingWeekDTO(null,"first week");
        RunningSessionDTO runningSessionDTO = new RunningSessionDTO(null, "first running session", LocalTime.now(), "easy run", 100, 200);
        trainingWeekDTO.getTrainingSessions().add(runningSessionDTO);
        WeightSessionDTO weightSessionDTO = new WeightSessionDTO(null, "first weight session", LocalTime.now(), "Load..." );
        trainingWeekDTO.getTrainingSessions().add(weightSessionDTO);
        RunningSeriesDTO runningSeriesDTO = new RunningSeriesDTO(null,23,23,LocalTime.now(), 12, 0);
        runningSessionDTO.getRunningSeries().add(runningSeriesDTO);
        extendedTrainingPlanDTO.getTrainingWeeks().add(trainingWeekDTO);

        Mockito.when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(java.util.Optional.ofNullable(trainingPlan));
        trainingPlanService.saveExistingTrainingPlan(extendedTrainingPlanDTO);
        assertEquals(trainingPlan.getTrainingWeeks().size(),1);
        assertEquals(trainingPlan.getTrainingWeeks().get(0).getTrainingSessions().size(),2);
        RunningSession runningSession = (RunningSession) trainingPlan.getTrainingWeeks().get(0).getTrainingSessions().get(0);
        assertEquals(runningSession.getRunningSeries().size(),1);
    }

    @Test
    public void ModifyTrainingPlanDeleteWeekSessionAndSerie(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trainer user = (Trainer) userDetails.getUser();

        TrainingPlan trainingPlan = new TrainingPlan(1L,"plan","road","marathon","description");
        trainingPlan.setTrainer(user);
        TrainingWeek trainingWeek = new TrainingWeek(1L,"first week");
        TrainingWeek trainingWeek2 = new TrainingWeek(2L,"second week");
        RunningSession runningSession = new RunningSession(1L, LocalTime.now(), "first running session", "easy run", 100, 200);
        WeightSession weightSession = new WeightSession(2L, LocalTime.now(), "first weight session", "Load..." );
        RunningSeries runningSeries = new RunningSeries(1L,23,23,LocalTime.now(), 12, 0);
        runningSession.getRunningSeries().add(runningSeries);
        trainingPlan.getTrainingWeeks().add(trainingWeek);
        trainingPlan.getTrainingWeeks().add(trainingWeek2);
        trainingPlan.getTrainingWeeks().get(0).getTrainingSessions().add(runningSession);
        trainingPlan.getTrainingWeeks().get(0).getTrainingSessions().add(weightSession);

        Mockito.when(trainingPlanRepository.findById(trainingPlan.getId())).thenReturn(java.util.Optional.ofNullable(trainingPlan));

        ExtendedTrainingPlanDTO extendedTrainingPlanDTO = new ExtendedTrainingPlanDTO(1L,"plan","road","marathon","description");
        TrainingWeekDTO trainingWeekDTO = new TrainingWeekDTO(1L,"first week");
        WeightSessionDTO weightSessionDTO = new WeightSessionDTO(2L, "first weight session", LocalTime.now(), "Load..." );
        trainingWeekDTO.getTrainingSessions().add(weightSessionDTO);
        extendedTrainingPlanDTO.getTrainingWeeks().add(trainingWeekDTO);

        trainingPlanService.saveExistingTrainingPlan(extendedTrainingPlanDTO);

        assertEquals(trainingPlan.getTrainingWeeks().size(),1);
        assertEquals(trainingPlan.getTrainingWeeks().get(0).getTrainingSessions().size(),1);
        assertEquals(trainingPlan.getTrainingWeeks().get(0).getTrainingSessions().get(0).getTypeOfSession(), "Weight");
    }

    @Test
    public void SaveTrainingPlanWithoutClub(){
        TrainingPlan trainingPlanDB;
        TrainingPlan trainingPlan = new TrainingPlan(null,"plan","road","marathon","description");

        Mockito.when(trainingPlanRepository.save(trainingPlan)).then(i -> i.getArguments()[0]);
        trainingPlanService.saveNewTrainingPlan(trainingPlan,Optional.empty());
    }

    @Test
    public void SaveTrainingPlanWithClub(){
        TrainingPlan trainingPlanDB;
        TrainingPlan trainingPlan = new TrainingPlan(null,"plan","road","marathon","description");
        Club club = new Club();
        club.setId(1L);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trainer user = (Trainer) userDetails.getUser();
        club.setTrainerCreator(user);

        Mockito.when(trainingPlanRepository.save(trainingPlan)).then(i -> i.getArguments()[0]);
        Optional<Long> clubId = Optional.of(1L);
        Mockito.when(clubService.loadClubById(clubId.get())).thenReturn(club);
        trainingPlanService.saveNewTrainingPlan(trainingPlan, clubId);
    }

    @Test
    public void SaveTrainingPlanWithClubButNotCreator(){
        TrainingPlan trainingPlanDB;
        TrainingPlan trainingPlan = new TrainingPlan(null,"plan","road","marathon","description");
        Club club = new Club();
        club.setId(1L);

        Trainer user = new Trainer();
        user.setUsername("notLogged");
        club.setTrainerCreator(user);

        Mockito.when(trainingPlanRepository.save(trainingPlan)).then(i -> i.getArguments()[0]);
        Optional<Long> clubId = Optional.of(1L);
        Mockito.when(clubService.loadClubById(clubId.get())).thenReturn(club);

        try {
            trainingPlanService.saveNewTrainingPlan(trainingPlan, clubId);
            fail("Exception not found");
        }
        catch (ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }

    @Test
    public void SaveTrainingPlanAthlete(){
        Athlete ath = new Athlete();
        ath.setUsername("athleteTest");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.setUser(ath);

        TrainingPlan trainingPlan = new TrainingPlan(null,"plan","road","marathon","description");
        try{
            trainingPlanService.saveNewTrainingPlan(trainingPlan,Optional.empty());
        }
        catch (ResponseStatusException e){
            assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }
}
