package com.bt.guestbook.service.impl;

import com.bt.guestbook.model.Comment;
import com.bt.guestbook.repository.CommentRepository;
import com.bt.guestbook.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl  implements CommentService {

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Boolean delete(int id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Comment update(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(int id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public List<Comment> findAll() {
        return (List<Comment>) commentRepository.findAll();
    }

    @Override
    public List<Comment> findByStatus(String status) {
        return commentRepository.findByStatus(status);
    }

    @Override
    public List<Comment> findByUserIdStatus(int userId, String status) {
        return commentRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Comment> findBetween(int start, int end) {
        return commentRepository.findBetween(start, end);
    }
}
