package com.example.webserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResponse {
    private CommentEntity commentEntity;
    private String message;
}
