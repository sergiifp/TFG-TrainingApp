package com.example.TFGtraining.Club;

import com.example.TFGtraining.DTO.ClubDTO;
import com.example.TFGtraining.DTO.EventDTO;
import com.example.TFGtraining.Event.Event;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.Trainer.Trainer;
import com.example.TFGtraining.TrainingPlan.TrainingPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;

    public void createClub(String name, String password){
        if (name == null || name.equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The name of the club is not valid");
        }
        if(password == null || password.equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password of the club is not valid");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordEncoded = passwordEncoder.encode(password);
            Club club = new Club(name, passwordEncoded, (Trainer) userDetails.getUser());
            clubRepository.save(club);
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already exists one club with this name, please try with another");
        }
    }

    public void deleteClub(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUser().getType().equals("Trainer")){
            Optional<Club> optClub  = clubRepository.findById(id);
            if (!optClub.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The club with id "+ id +" don't exists");
            Club club = optClub.get();
            if (!userDetails.getUsername().equals(club.getTrainerCreator().getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this club");
            }
            clubRepository.delete(club);
        }
        else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not a Trainer");
        }
    }

    public Club loadClubById(Long clubId) {
        Optional<Club> optClub  = clubRepository.findById(clubId);
        if (!optClub.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The club with id "+ clubId +" does not exists");
        return optClub.get();
    }

    public List<TrainingPlan> getClubTrainingPlans(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Club> optClub  = clubRepository.findById(id);
        if (!optClub.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The club with id "+ id +" does not exists");
        Club club = optClub.get();
        if (userDetails.getUser().getType().equals("Trainer") && !userDetails.getUser().getUsername().equals(club.getTrainerCreator().getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't take part of this club");
        }
        if (userDetails.getUser().getType().equals("Athlete")){
            boolean found = false;
            int i = 0;
            while (!found && i < club.getEnrolledAthletes().size()){
                if (club.getEnrolledAthletes().get(i).getUsername().equals(userDetails.getUser().getUsername())){
                    found = true;
                }
                ++i;
            }
            if(!found){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't take part of this club");
            }
        }
        return club.getCustomPlans();
    }

    public List<EventDTO> getClubEvents(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Club> optClub  = clubRepository.findById(id);
        if (!optClub.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The club with id "+ id +" does not exists");
        Club club = optClub.get();
        if (userDetails.getUser().getType().equals("Trainer") && !userDetails.getUser().getUsername().equals(club.getTrainerCreator().getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't take part of this club");
        }
        if (userDetails.getUser().getType().equals("Athlete")){
            boolean found = false;
            int i = 0;
            while (!found && i < club.getEnrolledAthletes().size()){
                if (club.getEnrolledAthletes().get(i).getUsername().equals(userDetails.getUser().getUsername())){
                    found = true;
                }
                ++i;
            }
            if(!found){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't take part of this club");
            }
        }
        List<EventDTO> events = new ArrayList<>();
        for (Event event: club.getEvents()){
            events.add(new EventDTO(event));
        }
        return events;
    }

    public ClubDTO getClub(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Club> optClub  = clubRepository.findById(id);
        if (!optClub.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The club with id "+ id +" does not exists");
        Club club = optClub.get();
        if (userDetails.getUser().getType().equals("Trainer") && !userDetails.getUser().getUsername().equals(club.getTrainerCreator().getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't take part of this club");
        }
        if (userDetails.getUser().getType().equals("Athlete")){
            boolean found = false;
            int i = 0;
            while (!found && i < club.getEnrolledAthletes().size()){
                if (club.getEnrolledAthletes().get(i).getUsername().equals(userDetails.getUser().getUsername())){
                    found = true;
                }
                ++i;
            }
            if(!found){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't take part of this club");
            }
        }
        return new ClubDTO(club);
    }
}
