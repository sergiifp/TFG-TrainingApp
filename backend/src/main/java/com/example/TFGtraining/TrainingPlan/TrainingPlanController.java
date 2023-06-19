package com.example.TFGtraining.TrainingPlan;

import com.example.TFGtraining.DTO.ExtendedTrainingPlanDTO;
import com.example.TFGtraining.DTO.TrainingPlanDTO;
import com.example.TFGtraining.Mapper.TrainingPlanMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainingplans")
public class TrainingPlanController {
    @Autowired
    private TrainingPlanService trainingPlanService;

    @GetMapping
    public List<TrainingPlanDTO> getAllTrainingPlans(){
        List<TrainingPlan> list = trainingPlanService.getTrainingPLansWithoutClub();
        List<TrainingPlanDTO> DTOlist = new ArrayList<>();
        for (TrainingPlan t: list){
            TrainingPlanDTO tempDTO = new TrainingPlanDTO(t);
            DTOlist.add(tempDTO);
        }
        return DTOlist;
    }

    @GetMapping("/{id}")
    public ExtendedTrainingPlanDTO getTrainingPlan(@PathVariable Long id){
        return trainingPlanService.getTrainingPlanDTOById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTrainingPlan(@Valid @RequestBody ExtendedTrainingPlanDTO trainingPlanDTO, @RequestParam Optional<Long> clubId){
        TrainingPlanMapper trainingPlanMapper = TrainingPlanMapper.getInstance();
        TrainingPlan trainingPlan = trainingPlanMapper.toEntity(trainingPlanDTO);
        this.trainingPlanService.saveNewTrainingPlan(trainingPlan, clubId);}

    @PutMapping(consumes = "application/json")
    public void saveTrainingPlan(@Valid @RequestBody ExtendedTrainingPlanDTO trainingPlan){
        this.trainingPlanService.saveExistingTrainingPlan(trainingPlan);}

    @PostMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLike(@PathVariable Long id){
        this.trainingPlanService.addLike(id);
    }

    @DeleteMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long id){
        this.trainingPlanService.deleteLike(id);
    }
}
