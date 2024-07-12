package com.example.webserver;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Slf4j
@Entity
@Table(name = "boards")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id")
    private long boardId;

    @OneToMany(mappedBy = "board")
    private List<CommentEntity> comments;

    @Column(name = "board_writer")
    private String boardWriter;

    @Column(name = "board_date")
    private LocalDateTime boardDate;

    @Column(name = "board_count")
    private long boardCount;

    @Column(name = "board_content")
    private String boardContent;
}
