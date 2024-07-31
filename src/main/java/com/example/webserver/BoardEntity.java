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
    @Column(name="id")
    private long boardId;

    @Column(name = "title", nullable = false)
    private String boardTitle;

    @Column(name = "writer", nullable = false)
    private String boardWriter;

    @Column(name = "writing_time", nullable = false)
    private LocalDateTime boardWritingDate;

    @Column(name = "reading_count", nullable = false)
    private Integer boardReadingCount;

    @Column(name = "text_content", nullable = false)
    private String boardTextContent;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<CommentEntity> boardComments;
}
