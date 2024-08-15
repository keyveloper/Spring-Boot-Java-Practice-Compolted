package com.example.webserver.dto;

import com.example.webserver.entity.CommentEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@RequiredArgsConstructor
public class GetBoardResponseDto {
    private long id;

    private String title;

    private String writer;

    private LocalDateTime writingTime;

    private Integer readingCount;

    private String textContent;

    private List<GetBoardCommentDto> comments;
}
