package com.example.webserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Slf4j
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EntityListeners(AuditingEntityListener.class)
@Data
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private final long id;

    @ManyToOne
    @JoinColumn(name="board_id")
    @JsonBackReference
    @ToString.Exclude
    private final BoardEntity board;

    @Column(name="writer")
    private String writer;

    @CreatedDate
    @Column(name="writing_time")
    private final LocalDateTime writingTime;

    @LastModifiedDate
    @Column(name="last_modified_time")
    private LocalDateTime lastModifiedTime;

    @Column(name="text_content")
    private String textContent;
}
