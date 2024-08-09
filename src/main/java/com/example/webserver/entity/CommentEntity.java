package com.example.webserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Slf4j
@Table(name = "comments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne
    @JoinColumn(name="board_id")
    @JsonBackReference
    @ToString.Exclude
    private BoardEntity board;

    @Column(name="writer")
    private String writer;

    @Column(name="writing_time")
    private LocalDateTime writingTime;

    @Column(name="text_content")
    private String textContent;
}
