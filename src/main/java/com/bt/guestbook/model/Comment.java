package com.bt.guestbook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.config.Task;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity()
@Table(name = "comments", schema = "guestbookdb")
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "description")
    private String description;


    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "status")
    private String status;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "approval")
    private String approval;

    @Column(nullable = true, length = 64)
    private String photos;

    @Transient
    public String getPhotosImagePath() {
        if (photos == null ) return null;

        //return "/user-photos/" + id + "/" + photos;
        return "/user-photos/" + photos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                userId == comment.userId &&
                Objects.equals(description, comment.description) &&
                Objects.equals(createDate, comment.createDate) &&
                Objects.equals(updateDate, comment.updateDate) &&
                Objects.equals(status, comment.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, description,  createDate, status, userId);
    }

}
