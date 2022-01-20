package com.bt.guestbook.repository;

import com.bt.guestbook.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    List<Comment> findByStatus(String status);

    List<Comment> findByUserIdAndStatus(int userId, String status);

    @Query("from Comment c where c.id between :start and :end")
    List<Comment> findBetween(@Param("start") int start, @Param("end") int end);
}
