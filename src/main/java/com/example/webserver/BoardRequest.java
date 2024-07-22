package com.example.webserver;

import lombok.Data;

@Data
public class BoardRequest {
    private String writer;
    private String content;
}
