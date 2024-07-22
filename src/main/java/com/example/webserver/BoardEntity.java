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

    @Column(name = "board_writer", nullable = false)
    private String boardWriter;

    @Column(name = "board_date", nullable = false)
    private LocalDateTime boardDate;

    @Column(name = "board_count", nullable = false)
    private Integer boardCount;

    @Column(name = "board_content", nullable = false)
    private String boardContent;
}
