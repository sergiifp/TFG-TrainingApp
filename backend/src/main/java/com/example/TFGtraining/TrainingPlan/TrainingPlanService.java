package com.example.TFGtraining.TrainingPlan;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.Club.ClubService;
import com.example.TFGtraining.DTO.*;
import com.example.TFGtraining.Mapper.TrainingPlanMapper;
import com.example.TFGtraining.RunningSession.RunningSeries;
import com.example.TFGtraining.RunningSession.RunningSeriesRepository;
import com.example.TFGtraining.RunningSession.RunningSession;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.Trainer.Trainer;
import com.example.TFGtraining.TrainingSession.TrainingSession;
import com.example.TFGtraining.TrainingSession.TrainingSessionRepository;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import com.example.TFGtraining.TrainingWeek.TrainingWeekRepository;
import com.example.TFGtraining.User.User;
import com.example.TFGtraining.User.UserService;
import com.example.TFGtraining.WeightSession.WeightSession;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingPlanService {

    @Autowired
    TrainingPlanRepository trainingPlanRepository;

    @Autowired
    private ClubService clubService;
    @Autowired
    private UserService userService;

    @Autowired
    private TrainingWeekRepository trainingWeekRepository;
    @Autowired
    private TrainingSessionRepository trainingSessionRepository;
    @Autowired
    private RunningSeriesRepository runningSeriesRepository;



    public List<TrainingPlan> getTrainingPLansWithoutClub() {
        return (List<TrainingPlan>) trainingPlanRepository.findAllByOwnerClubIsNull();
    }

    public TrainingPlan getTrainingPlanById(Long id){
        Optional<TrainingPlan> trainingPlan = this.trainingPlanRepository.findById(id);
        if (trainingPlan.isPresent()){
            return trainingPlan.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training plan with id "+ id +" does not exist" );
        }
    }

    public ExtendedTrainingPlanDTO getTrainingPlanDTOById(Long id){
        Optional<TrainingPlan> trainingPlan = this.trainingPlanRepository.findById(id);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (trainingPlan.isPresent()){
            TrainingPlan trainingPlanEntity = trainingPlan.get();

            if (userDetails.getUser().getType().equals("Trainer") && (!userDetails.getUsername().equals(trainingPlanEntity.getTrainer().getUsername()))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are a Trainer and you are not the owner of this training plan");
            }
            if (trainingPlanEntity.getOwnerClub() != null){
                System.out.println("this plan is in a club");
                User user = userService.getUser(userDetails.getUsername());
                if (userDetails.getUser().getType().equals("Trainer")){
                    Trainer trainer = (Trainer) user;
                    if (!trainer.getCreatedClubs().contains(trainingPlanEntity.getOwnerClub())){
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This plan belongs to a club and you are not the owner of this club");
                    }
                }
                else{
                    Athlete athlete = (Athlete) user;
                    if (!trainingPlanEntity.getOwnerClub().getEnrolledAthletes().contains(athlete)){
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This plan belongs to a club and you don't take part of this club");
                    }
                }
            }
            TrainingPlanMapper trainingPlanMapper = TrainingPlanMapper.getInstance();
            return trainingPlanMapper.toDTO(trainingPlanEntity);
        }else {
            throw new EntityNotFoundException();
        }
    }

    public void saveNewTrainingPlan(TrainingPlan trainingPlan, Optional<Long> clubId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUser().getType().equals("Trainer")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
        }
        Trainer trainer = (Trainer) userDetails.getUser();
        trainingPlan.setTrainer(trainer);
        if (clubId.isPresent()){
            Club club = clubService.loadClubById(clubId.get());
            if (!club.getTrainerCreator().getUsername().equals(userDetails.getUser().getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this club");
            }
            trainingPlan.setOwnerClub(club);
        }
        trainingPlanRepository.save(trainingPlan);
    }
    public void saveExistingTrainingPlan(ExtendedTrainingPlanDTO trainingPlan){
        Optional<TrainingPlan> trainingPlanDbOpt = trainingPlanRepository.findById(trainingPlan.getId());
        if (trainingPlanDbOpt.isPresent()){
            TrainingPlan trainingPlanDb = trainingPlanDbOpt.get();
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!userDetails.getUser().getType().equals("Trainer")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
            }
            if (!userDetails.getUsername().equals(trainingPlanDb.getTrainer().getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this training plan");
            }
            trainingPlanDb.setObjective(trainingPlan.getObjective());
            trainingPlanDb.setName(trainingPlan.getName());
            trainingPlanDb.setTypeOfActivity(trainingPlan.getTypeOfActivity());
            trainingPlanDb.setDescription(trainingPlan.getDescription());
            //TRAINING WEEKS
            int i = 0;
            while (i < trainingPlanDb.getTrainingWeeks().size()){
                TrainingWeek trainingWeekDb = trainingPlanDb.getTrainingWeeks().get(i);
                TrainingWeekDTO trainingWeek = trainingPlan.getTrainingWeeks().stream().filter(tp -> (tp.getId()!=null && tp.getId().equals(trainingWeekDb.getId()))).findAny().orElse(null);
                if (trainingWeek != null){
                    trainingWeekDb.setDescription(trainingWeek.getDescription());
                    //TRAINING SESSIONS
                    savetrainingSessions(trainingWeekDb, trainingWeek.getTrainingSessions());
                    trainingPlan.getTrainingWeeks().remove(trainingWeek);
                }
                else{
                    trainingPlanDb.getTrainingWeeks().remove(trainingWeekDb);
                    trainingWeekRepository.delete(trainingWeekDb);
                    --i;
                }
                ++i;
            }
            for (TrainingWeekDTO trainingWeek: trainingPlan.getTrainingWeeks()){
                if (trainingWeek.getId() != null){
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This week with id "+ trainingWeek.getId() + " does not belong to this training plan");
                }
                TrainingWeek weekToSave = new TrainingWeek();
                weekToSave.setDescription(trainingWeek.getDescription());
                weekToSave.setTrainingPlan(trainingPlanDb);
                savetrainingSessions(weekToSave, trainingWeek.getTrainingSessions());
                trainingPlanDb.getTrainingWeeks().add(weekToSave);
            }

            trainingPlanRepository.save(trainingPlanDb);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training plan with id "+ trainingPlan.getId() +" does not exist");
        }
    }

    public void savetrainingSessions(TrainingWeek trainingWeekDb, List<TrainingSessionDTO> trainingSessions){
        List<TrainingSession> trainingSessionsDb = trainingWeekDb.getTrainingSessions();
        int j = 0;
        while (j < trainingSessionsDb.size()){
            TrainingSession trainingSessionDb = trainingSessionsDb.get(j);
            TrainingSessionDTO trainingSession = trainingSessions.stream().filter(ts -> (ts.getId()!=null && ts.getId().equals(trainingSessionDb.getId()))).findAny().orElse(null);
            if (trainingSession != null) {
                trainingSessionDb.setDescription(trainingSession.getDescription());
                trainingSessionDb.setTime(trainingSession.getTime());
                if (trainingSessionDb.getTypeOfSession().equals("Running")) {
                    RunningSession runningSessionDb = (RunningSession) trainingSessionDb;
                    RunningSessionDTO runningSession = (RunningSessionDTO) trainingSession;
                    runningSessionDb.setNegativeSlope(runningSession.getNegativeSlope());
                    runningSessionDb.setPositiveSlope(runningSession.getPositiveSlope());
                    runningSessionDb.setType(runningSession.getType());
                    //RUNNING SERIES
                    saveRunningSeries(runningSessionDb.getRunningSeries(), runningSession.getRunningSeries());
                } else {
                    WeightSession weightSessionDb = (WeightSession) trainingSessionDb;
                    WeightSessionDTO weightSession = (WeightSessionDTO) trainingSession;
                    weightSessionDb.setLoad(weightSession.getLoad());
                }
                trainingSessions.remove(trainingSession);
            }
            else{
                trainingSessionsDb.remove(trainingSessionDb);
                trainingSessionRepository.delete(trainingSessionDb);
                --j;
            }
            ++j;
        }
        for(TrainingSessionDTO trainingSession: trainingSessions){
            if (trainingSession.getId() != null){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This training session with id "+ trainingSession.getId() + "does not belong to this training week");
            }
            if (trainingSession.getClass().isAssignableFrom(RunningSessionDTO.class)){
                RunningSession trainingSessionToSave = new RunningSession();
                trainingSessionToSave.setId(trainingSession.getId());
                trainingSessionToSave.setTime(trainingSession.getTime());
                trainingSessionToSave.setDescription(trainingSession.getDescription());
                RunningSessionDTO runningSession = (RunningSessionDTO) trainingSession;
                trainingSessionToSave.setPositiveSlope(runningSession.getPositiveSlope());
                trainingSessionToSave.setNegativeSlope(runningSession.getNegativeSlope());
                trainingSessionToSave.setType(runningSession.getType());
                trainingSessionToSave.setTrainingWeek(trainingWeekDb);
                saveRunningSeries(trainingSessionToSave.getRunningSeries(), runningSession.getRunningSeries());
                trainingSessionsDb.add(trainingSessionToSave);
            }
            else{
                WeightSession trainingSessionToSave = new WeightSession();
                trainingSessionToSave.setId(trainingSession.getId());
                trainingSessionToSave.setTime(trainingSession.getTime());
                trainingSessionToSave.setDescription(trainingSession.getDescription());
                trainingSessionToSave.setTrainingWeek(trainingWeekDb);
                WeightSessionDTO weightSession = (WeightSessionDTO) trainingSession;
                trainingSessionToSave.setLoad(weightSession.getLoad());
                trainingSessionsDb.add(trainingSessionToSave);
            }
        }
    }

    public void saveRunningSeries(List<RunningSeries> runningSeriesDb, List<RunningSeriesDTO> runningSeries){
        int k = 0;
        while (k < runningSeriesDb.size()){
            RunningSeries runningSerieDb = runningSeriesDb.get(k);
            RunningSeriesDTO runningSerie = runningSeries.stream().filter(rs -> (rs.getId()!=null && rs.getId().equals(runningSerieDb.getId()))).findAny().orElse(null);
            if (runningSerie != null){
                runningSerieDb.setDistance(runningSerie.getDistance());
                runningSerieDb.setRepetitions(runningSerie.getRepetitions());
                runningSerieDb.setFc(runningSerie.getFc());
                runningSerieDb.setRestTime(runningSerie.getRestTime());
                runningSerieDb.setReturnDistance(runningSerie.getReturnDistance());
                runningSeries.remove(runningSerie);
            }
            else{
                runningSeriesDb.remove(runningSerieDb);
                runningSeriesRepository.delete(runningSerieDb);
                --k;
            }
            ++k;
        }
        for (RunningSeriesDTO runningSerie: runningSeries){
            if (runningSerie.getId() != null){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This serie with id "+ runningSerie.getId() + "does not belong to this running session");
            }
            RunningSeries runningSerieToSave = new RunningSeries(runningSerie.getId(), runningSerie.getDistance(), runningSerie.getReturnDistance(),
                                                                runningSerie.getRestTime(), runningSerie.getRepetitions(), runningSerie.getFc());
            runningSeriesDb.add(runningSerieToSave);
        }
    }


    public void addLike(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        TrainingPlan trainingPlan = this.getTrainingPlanById(id);
        if (trainingPlan.getOwnerClub() != null) {
            User user = userService.getUser(userDetails.getUsername());
            Athlete athlete = (Athlete) user;
            if (!trainingPlan.getOwnerClub().getEnrolledAthletes().contains(athlete)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This plan belongs to a club and you don't take part of this club");
            }
        }
        Athlete athlete = trainingPlan.getLikedBy().stream().filter(u-> u.getUsername().equals(userDetails.getUsername())).findAny().orElse(null);
        if (athlete != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already liked it");
        }
        trainingPlan.getLikedBy().add((Athlete) userDetails.getUser());
        trainingPlanRepository.save(trainingPlan);
    }

    public void deleteLike(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not an Athlete");
        }
        TrainingPlan trainingPlan = this.getTrainingPlanById(id);
        if (trainingPlan.getOwnerClub() != null) {
            User user = userService.getUser(userDetails.getUsername());
            Athlete athlete = (Athlete) user;
            if (!trainingPlan.getOwnerClub().getEnrolledAthletes().contains(athlete)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This plan belongs to a club and you don't take part of this club");
            }
        }
        Athlete athlete = trainingPlan.getLikedBy().stream().filter(u-> u.getUsername().equals(userDetails.getUsername())).findAny().orElse(null);
        if (athlete == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You haven't liked it yet");
        }
        trainingPlan.getLikedBy().remove(athlete);
        trainingPlanRepository.save(trainingPlan);
    }

    public TrainingWeek loadTrainingWeek(Long id){
        Optional<TrainingWeek> trainingWeekOptional = trainingWeekRepository.findById(id);
        if (trainingWeekOptional.isPresent()){
            return trainingWeekOptional.get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The training week does not exist");
        }
    }

    public TrainingSession getTrainingSessionById(Long id) {
        Optional<TrainingSession> session = trainingSessionRepository.findById(id);
        if (session.isPresent()){
            return session.get();
        }
        else{
            throw new EntityNotFoundException();
        }

    }
}
