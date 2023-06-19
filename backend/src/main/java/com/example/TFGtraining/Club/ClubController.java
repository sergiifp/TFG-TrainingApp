package com.example.TFGtraining.Club;

import com.example.TFGtraining.DTO.ClubDTO;
import com.example.TFGtraining.DTO.EventDTO;
import com.example.TFGtraining.DTO.TrainingPlanDTO;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @PostMapping
    public void createClub(@RequestBody Map<String,String> body){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUser().getType().equals("Trainer")){
            clubService.createClub(body.get("name"), body.get("password"));
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a Trainer");
        }
    }

    @GetMapping("/{id}")
    public ClubDTO getClub(@PathVariable Long id){
        return clubService.getClub(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClub(@PathVariable Long id){
        clubService.deleteClub(id);
    }

    @GetMapping("/{id}/trainingplans")
    public List<TrainingPlanDTO> getTrainingplans(@PathVariable Long id){
        List<TrainingPlanDTO> list = new ArrayList<>();
        for (TrainingPlan t: clubService.getClubTrainingPlans(id)){
            list.add(new TrainingPlanDTO(t));
        }
        return list;
    };

    @GetMapping("/{id}/events")
    public List<EventDTO> getClubEvents(@PathVariable Long id){
        return clubService.getClubEvents(id);
    }
}
