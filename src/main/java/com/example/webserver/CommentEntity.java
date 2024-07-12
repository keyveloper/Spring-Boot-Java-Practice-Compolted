package com.example.webserver;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Slf4j
@Table(name = "comments")
@Data
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private long commentId;

    @ManyToOne
    @JoinColumn(name="comment_board_id")
    private BoardEntity board;

    @Column(name="comment_writer")
    private String commentWriter;

    @Column(name="comment_date")
    private LocalDateTime commentDate;

    @Column(name="comment_content")
    private String commentContent;
}
