package com.example.webserver.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpdateStatus {
    Ok("update Successfully"),
    Failed("update Failed");

    private final String message;
}
