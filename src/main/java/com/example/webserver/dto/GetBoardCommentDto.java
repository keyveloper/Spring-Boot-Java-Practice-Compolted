package com.example.webserver.dto;

import com.example.webserver.entity.BoardEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class GetBoardCommentDto {
    private long id;

    private String writer;

    private LocalDateTime writingTime;

    private String textContent;
}
