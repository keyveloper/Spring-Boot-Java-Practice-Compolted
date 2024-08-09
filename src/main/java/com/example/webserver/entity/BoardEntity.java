package com.example.webserver.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Slf4j
@Entity
@Table(name = "boards")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CommentEntity> comments;
}
