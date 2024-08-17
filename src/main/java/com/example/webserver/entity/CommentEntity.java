package com.example.webserver.entity;

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
    @Column(name="id")
    private long commentId;

    @ManyToOne
    @JoinColumn(name="board_id")
    private BoardEntity board;

    @Column(name="writer")
    private String Writer;

    @Column(name="writing_time")
    private LocalDateTime writingTime;

    @Column(name="text_content")
    private String textContent;
}
