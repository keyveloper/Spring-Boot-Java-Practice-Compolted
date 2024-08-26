package com.example.webserver.dto;

import com.example.webserver.enums.UpdateStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateResultDto {
    private final UpdateStatus updateStatus;
    private final long id;
}
