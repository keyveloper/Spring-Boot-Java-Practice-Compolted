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
    private final long id;

    private final String writer;

    private final LocalDateTime writingTime;

    private final String textContent;
}
