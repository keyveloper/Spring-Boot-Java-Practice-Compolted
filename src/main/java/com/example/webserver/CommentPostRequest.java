package com.example.webserver;

import lombok.Data;

@Data
public class CommentPostRequest {
    Integer boardID;
    String writer;
    String content;
}
