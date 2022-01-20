package com.bt.guestbook.repository;

import com.bt.guestbook.model.Comment;
import com.bt.guestbook.utils.Approval;
import com.bt.guestbook.utils.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommentRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CommentRepository repository;


    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateComment() {
        Comment comment = new Comment();
        comment.setApproval(Approval.UNAPPROVED.getValue());
        comment.setUserId(1);
        comment.setStatus(Status.PASSIVE.getValue());
        comment.setDescription("test comment");
        Comment savedComment =  repository.save(comment);
        Comment existingComment = entityManager.find(Comment.class, savedComment.getId());
        assertTrue(existingComment.equals(savedComment));

    }

    @Test
    @Order(2)
    public void testFindCommentByName() {
        Optional<Comment> comment = repository.findById(1);
        assertTrue(comment.get().getId() == 1);
    }

    @Test
    @Order(3)
    public void testListComments() {
        List<Comment> comments = (List<Comment>) repository.findAll();
        assertTrue(comments.size() > 0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateComment() {
        Optional<Comment> comment = repository.findById(1);
        comment.get().setApproval(Approval.APPROVE.getValue());
        repository.save(comment.get());
        Optional<Comment> updatedComment = repository.findById(1);
        assertEquals(comment, updatedComment);
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteComment() {
       Optional<Comment> comment = repository.findById(1);
       repository.deleteById(comment.get().getId());
       Optional<Comment> deletedComment = repository.findById(1);
        assertNull(deletedComment);
    }
}