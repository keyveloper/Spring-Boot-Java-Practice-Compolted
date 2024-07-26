package com.example.webserver;

import lombok.Data;

@Data
public class BoardPostRequest {
    private String title;
    private String writer;
    private String content;
}
