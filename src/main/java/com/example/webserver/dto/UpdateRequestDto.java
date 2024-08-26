package com.example.webserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateRequestDto {
    private final long id;
    private String textContent;
    private String title;
    private String writer;
}
