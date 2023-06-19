package com.example.TFGtraining.Comments;

import com.example.TFGtraining.Athlete.Athlete;
import com.example.TFGtraining.Club.Club;
import com.example.TFGtraining.DTO.CommentDTO;
import com.example.TFGtraining.Security.UserDetailsImpl;
import com.example.TFGtraining.TrainingPlan.TrainingPlanService;
import com.example.TFGtraining.TrainingWeek.TrainingWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TrainingPlanService trainingPlanService;


    public void createComment(Long weekId, String message) {
        Date date = new Date();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TrainingWeek trainingWeek = trainingPlanService.loadTrainingWeek(weekId);
        if (message == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message can't be null");
        }

        if (userDetails.getUser().getType().equals("Athlete")){
            Club club = trainingWeek.getTrainingPlan().getOwnerClub();
            if (club != null){
                Athlete athlete = club.getEnrolledAthletes().stream().filter(a->a.getUsername().equals(userDetails.getUsername())).findAny().orElse(null);
                if (athlete == null){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This plan belongs to a club and you don't take part of this club");
                }
            }
        }
        else{
            if (!trainingWeek.getTrainingPlan().getTrainer().getUsername().equals(userDetails.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this training plan");
            }
        }

        Comment comment = new Comment(trainingWeek, userDetails.getUser(), message, date);
        commentRepository.save(comment);
    }

    public List<CommentDTO> getComments(Long weekId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TrainingWeek trainingWeek = trainingPlanService.loadTrainingWeek(weekId);
        if (userDetails.getUser().getType().equals("Athlete")){
            Club club = trainingWeek.getTrainingPlan().getOwnerClub();
            if (club != null){
                Athlete athlete = club.getEnrolledAthletes().stream().filter(a->a.getUsername().equals(userDetails.getUsername())).findAny().orElse(null);
                if (athlete == null){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This plan belongs to a club and you don't take part of this club");
                }
            }
        }
        else{
            if (!trainingWeek.getTrainingPlan().getTrainer().getUsername().equals(userDetails.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this training plan");
            }
        }
        List<Comment> list = this.commentRepository.findAllByTrainingWeek_id(weekId);
        List<CommentDTO> listDTO = new ArrayList();
        for(Comment c: list){
            listDTO.add(new CommentDTO(c.getId(), c.getUser().getUsername(), c.getCreationDate(), c.getMessage()));
        }
        return listDTO;
    }
}
