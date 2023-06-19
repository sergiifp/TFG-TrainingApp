package com.example.TFGtraining.Comments;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository  extends CrudRepository<Comment,Long> {
     List<Comment> findAllByTrainingWeek_id(Long weekId);
}
