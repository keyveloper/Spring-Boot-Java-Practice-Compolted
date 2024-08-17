package com.example.webserver.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long boardId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "writer", nullable = false)
    private String writer;

    @Column(name = "writing_time", nullable = false)
    private LocalDateTime writingTime;

    @Column(name = "reading_count", nullable = false)
    private Integer readingCount;

    @Column(name = "text_content", nullable = false)
    private String textContent;

    @OneToMany(mappedBy = "board")
    private List<CommentEntity> comments;
}
