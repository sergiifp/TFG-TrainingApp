package com.example.TFGtraining.Trainer;

import com.example.TFGtraining.DTO.ClubDTO;
import com.example.TFGtraining.DTO.ModifyTrainerDTO;
import com.example.TFGtraining.DTO.TrainerDTO;
import com.example.TFGtraining.DTO.TrainingPlanDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping("/{username}")
    public TrainerDTO getTrainer(@PathVariable String username){
        return this.trainerService.getTrainer(username);
    }

    @PutMapping("/{username}")
    public void modifyTrainer(@RequestBody @Valid ModifyTrainerDTO trainer, @PathVariable String username){this.trainerService.modifyTrainer(username, trainer);}

    @GetMapping("/{username}/trainingplans")
    public List<TrainingPlanDTO> getMyTrainingPlans(@PathVariable String username){
        return this.trainerService.getMyPlans(username);
    }

    @GetMapping("/{username}/clubs")
    public List<ClubDTO> getMyClubs(@PathVariable String username){
        return this.trainerService.getMyClubs(username);
    }

}
