package com.example.webserver;

import lombok.Data;

@Data
public class RequestComment {
    long boardId;
    String writer;
    String textContent;
}
