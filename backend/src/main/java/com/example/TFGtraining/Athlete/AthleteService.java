package com.example.TFGtraining.Athlete;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.Club.ClubService;
import com.example.TFGtraining.DTO.AthleteDTO;
import com.example.TFGtraining.DTO.ClubDTO;
import com.example.TFGtraining.DTO.ModifyAthleteDTO;
import com.example.TFGtraining.DTO.TrainingPlanDTO;
import com.example.TFGtraining.PlanProgress.PlanProgress;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.SessionResult.SessionResultService;
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
public class AthleteService {
    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    SessionResultService sessionResultService;
    @Autowired
    ClubService clubService;

    public AthleteDTO getAthlete(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }

        Athlete athlete = athleteRepository.findByUsername(username);
        List<PlanProgress> plans = athlete.getEnrolledPlans();
        Integer sessionsDone = 0;
        Integer totalKm = 0;
        for (PlanProgress plan: plans){
            sessionsDone += athlete.getResults().size();
        }
        totalKm = sessionResultService.getTotalKm(athlete.getResults());
        AthleteDTO dto = new AthleteDTO(athlete, plans.size(), sessionsDone, totalKm);
        return dto;
    }

    public void modifyAthlete(String username, ModifyAthleteDTO athleteRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        Athlete athlete = athleteRepository.findByUsername(username);
        if(athleteRequest.getPassword() != null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordEncoded = passwordEncoder.encode(athleteRequest.getPassword());
            athlete.setPassword(passwordEncoded);
        }
        if (athleteRequest.getDescription().equals("")){
            athlete.setDescription(null);
        }
        else athlete.setDescription(athleteRequest.getDescription());
        athlete.setAge(athleteRequest.getAge());
        athlete.setFcMax(athleteRequest.getFcMax());
        athlete.setWeight(athleteRequest.getWeight());
        athlete.setHeight(athleteRequest.getHeight());
        athleteRequest.setWeight(athleteRequest.getWeight());
        athleteRepository.save(athlete);
    }

    public void enrollToClub(String username, String password, Long clubId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (userDetails.getUser().getType().equals("Athlete")){
            Athlete athlete = athleteRepository.findByUsername(username);
            Club club = clubService.loadClubById(clubId);
            if (athlete.getEnrolledClubs().contains(club)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already member of this club");
            }
            else{
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                if (!passwordEncoder.matches(password,club.getPassword())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "The password is not correct");
                }
                athlete.getEnrolledClubs().add(club);
                athleteRepository.save(athlete);
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
    }

    public void unrollToClub(String username, Long clubId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (userDetails.getUser().getType().equals("Athlete")){
            Athlete athlete = athleteRepository.findByUsername(username);
            Club club = clubService.loadClubById(clubId);
            if (athlete.getEnrolledClubs().contains(club)){
                athlete.getEnrolledClubs().remove(club);
                athleteRepository.save(athlete);
            }
            else{
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You are not enrolled to this club");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
    }

    public List<ClubDTO> getEnrolledClubs(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        Athlete athlete = athleteRepository.findByUsername(userDetails.getUsername());
        List<ClubDTO> returnList = new ArrayList<>();
        for (Club club: athlete.getEnrolledClubs()){
            returnList.add(new ClubDTO(club));
        }
        return returnList;
    }

    public List<TrainingPlanDTO> getEnrolledPlans(String username){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        Athlete athlete = athleteRepository.findByUsername(username);
        List<PlanProgress> list = athlete.getEnrolledPlans();
        List<TrainingPlanDTO> DTOlist = new ArrayList<>();
        for (PlanProgress p: list){
            TrainingPlanDTO tempDTO = new TrainingPlanDTO(p.getPlan());
            DTOlist.add(tempDTO);
        }
        return DTOlist;
    }

    public List<TrainingPlanDTO> getLikedPlans(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The token doesn't match with user request");
        }
        if (!userDetails.getUser().getType().equals("Athlete")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an Athlete");
        }
        Athlete athlete = athleteRepository.findByUsername(username);
        List<TrainingPlanDTO> DTOlist = new ArrayList<>();
        for (TrainingPlan t: athlete.getLikedPlans()){
            DTOlist.add(new TrainingPlanDTO(t));
        }
        return DTOlist;
    }
}
