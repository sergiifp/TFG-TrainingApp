package com.example.TFGtraining.Comments;

import com.example.TFGtraining.DTO.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/trainingweeks/{weekId}/comments")
    public void createComment(@PathVariable Long weekId, @RequestBody Map<String,String> body){
        String message = body.get("message");
        this.commentService.createComment(weekId, message);
    }

    @GetMapping("/trainingweeks/{weekId}/comments")
    public List<CommentDTO> getComments(@PathVariable Long weekId){
        return this.commentService.getComments(weekId);
    }
}
