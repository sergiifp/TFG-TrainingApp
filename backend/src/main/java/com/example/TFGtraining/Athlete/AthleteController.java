package com.example.TFGtraining.Athlete;

import com.example.TFGtraining.DTO.AthleteDTO;
import com.example.TFGtraining.DTO.ClubDTO;
import com.example.TFGtraining.DTO.ModifyAthleteDTO;
import com.example.TFGtraining.DTO.TrainingPlanDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/athlete")
public class AthleteController {
    @Autowired
    private AthleteService athleteService;

    @GetMapping("/{username}")
    public AthleteDTO getAthlete(@PathVariable String username){
        return this.athleteService.getAthlete(username);
    }

    @PutMapping("/{username}")
    public void modifyAthlete(@RequestBody @Valid ModifyAthleteDTO athlete, @PathVariable String username){this.athleteService.modifyAthlete(username, athlete);}

    @PostMapping("/{username}/clubs/{clubId}")
    public void enrollToClub(@PathVariable String username, @RequestBody Map<String,String> body, @PathVariable Long clubId){
        athleteService.enrollToClub(username, body.get("password"), clubId);
    }

    @DeleteMapping("/{username}/clubs/{clubId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unrollToClub(@PathVariable String username, @PathVariable Long clubId){
        athleteService.unrollToClub(username, clubId);
    }

    @GetMapping("/{username}/clubs")
    public List<ClubDTO> getEnrolledClubs(@PathVariable String username){
        return athleteService.getEnrolledClubs(username);
    }

    @GetMapping("/{username}/trainingplans")
    public List<TrainingPlanDTO> getMyTrainingPlans(@PathVariable String username){
        return athleteService.getEnrolledPlans(username);
    }

    @GetMapping("/{username}/likedplans")
    public List<TrainingPlanDTO> getMyLikedPlans(@PathVariable String username){
        return athleteService.getLikedPlans(username);
    }
}
