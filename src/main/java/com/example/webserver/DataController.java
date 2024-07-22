package com.example.webserver;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DataController {
    private final BoardRepositoryImpl boardRepository;
    private final CommentRepositoryImpl commentRepository;
    private final BoardRepositoryImpl boardRepositoryImpl;

    @GetMapping("/boardAll")
    public ResponseEntity<List<BoardEntity>> getAllBoardEntity() {
        Optional<List<BoardEntity>> boards = boardRepositoryImpl.getAllBoard();

        return boards
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<Object> postBoard(@RequestBody BoardRequest boardRequest) {

        Optional<Long> resultId = boardRepositoryImpl.put(boardRequest.getWriter(), boardRequest.getContent());
        return resultId
                .map(id -> ResponseEntity.created(URI.create("http://localhost:8080/board/" + id)).build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }




}
