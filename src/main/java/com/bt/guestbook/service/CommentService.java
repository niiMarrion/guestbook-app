package com.bt.guestbook.service;


import com.bt.guestbook.model.Comment;

import java.util.List;

public interface CommentService {
    
    Comment save(Comment comment);

    Boolean delete(int id);

    Comment update(Comment comment);

    Comment findById(int id);

    List<Comment> findAll();

    List<Comment> findByStatus(String status);

    List<Comment> findByUserIdStatus(int userId, String status);

    List<Comment> findBetween(int start, int end);
}
