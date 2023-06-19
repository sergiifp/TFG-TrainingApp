package com.example.TFGtraining.Event;

import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.Club.ClubService;
import com.example.TFGtraining.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ClubService clubService;


    public void createEvent(Long clubId, String title, String description, Date eventDate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Club club = clubService.loadClubById(clubId);
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

        Event event  = new Event(title, description, eventDate);
        event.setClub(club);
        event.setUserCreator(userDetails.getUser());
        eventRepository.save(event);
    }
}
