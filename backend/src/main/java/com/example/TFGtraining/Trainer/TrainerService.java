package com.example.TFGtraining.Trainer;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.DTO.ClubDTO;
import com.example.TFGtraining.DTO.ModifyTrainerDTO;
import com.example.TFGtraining.DTO.TrainerDTO;
import com.example.TFGtraining.DTO.TrainingPlanDTO;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainerService {
    @Autowired
    private TrainerRepository trainerRepository;

    public TrainerDTO getTrainer(String username) {
        Trainer trainer = trainerRepository.findByUsername(username);
        TrainerDTO dto = new TrainerDTO(trainer);
        return dto;
    }

    public void modifyTrainer(String username, ModifyTrainerDTO trainerRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Trainer")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
        }
        Trainer trainer = trainerRepository.findByUsername(username);
        if (trainerRequest.getPassword() != null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordEncoded = passwordEncoder.encode(trainerRequest.getPassword());
            trainerRequest.setPassword(passwordEncoded);
        }
        trainer.setAge(trainerRequest.getAge());
        if (trainerRequest.getDescription().equals("")){
            trainer.setDescription(null);
        }
        else trainer.setDescription(trainerRequest.getDescription());
        trainer.setTypeOfActivityPlans(trainerRequest.getTypeOfActivityPlans());
        trainer.setLanguages(trainerRequest.getLanguages());
        trainerRepository.save(trainer);
    }

    public List<TrainingPlan> getTrainingPlans(String username){
        Trainer trainer = trainerRepository.findByUsername(username);
        return trainer.getTrainingPlans();
    }

    public List<ClubDTO> getMyClubs(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Trainer")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
        }
        Trainer trainer = trainerRepository.findByUsername(userDetails.getUsername());
        List<ClubDTO> returnList = new ArrayList<>();
        for (Club club: trainer.getCreatedClubs()){
            returnList.add(new ClubDTO(club));
        }
        return returnList;
    }

    public List<TrainingPlanDTO> getMyPlans(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Trainer")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
        }
        List<TrainingPlan> list = this.getTrainingPlans(username);
        List<TrainingPlanDTO> DTOlist = new ArrayList<>();
        for (TrainingPlan t: list){
            TrainingPlanDTO tempDTO = new TrainingPlanDTO(t);
            DTOlist.add(tempDTO);
        }
        return DTOlist;
    }
}
