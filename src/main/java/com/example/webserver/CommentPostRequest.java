package com.example.webserver;

import lombok.Data;

@Data
public class CommentPostRequest {
    long boardId;
    String writer;
    String textContent;
}
