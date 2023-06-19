package com.example.TFGtraining.PlanProgress;

import com.example.TFGtraining.DTO.PlanProgressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class PlanProgressController {
    @Autowired
    private PlanProgressService planProgressService;

    @GetMapping("athlete/{username}/trainingplans/{trainingPlanId}/planprogress")
    public PlanProgressDTO getProgressPlan(@PathVariable String username, @PathVariable Long trainingPlanId){
        return planProgressService.getPlanProgressByUsernameAndPlanId(username,trainingPlanId);
    }

    @GetMapping("trainingplans/{trainingPlanId}/planprogress")
    public List<PlanProgressDTO> getAllprogressPlans(@PathVariable Long trainingPlanId){
        return planProgressService.getPlanProgressByPlanId(trainingPlanId);
    }

    @PostMapping("trainingplans/{id}/planprogress")
    public void enrollToPlan(@PathVariable Long id){
        planProgressService.createPlanProgress(id);
    }

    @DeleteMapping("athlete/{username}/trainingplans/{trainingPlanId}/planprogress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable Long trainingPlanId, @PathVariable String username){
        planProgressService.deletePlanProgress(username, trainingPlanId);
    }
}
