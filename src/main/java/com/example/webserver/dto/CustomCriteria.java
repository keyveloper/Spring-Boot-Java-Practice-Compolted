package com.example.webserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class CustomCriteria {
    private final String writer;
    private final String notContainWriter;
    private final Integer minReads;
    private final Integer maxReads;
}
