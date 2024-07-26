package com.example.webserver;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardEntity> getBoard(@PathVariable("id") Long id) {
        Optional<BoardEntity> result = boardRepositoryImpl.get(id);
        return result
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<Object> postBoard(@RequestBody BoardPostRequest postRequest) {
        log.info("\ntitle: " + postRequest.getTitle() + "\nwriter: " + postRequest.getWriter()
                + "\ncontent: " + postRequest.getContent());
        Optional<Long> resultId = boardRepositoryImpl
                .put(postRequest.getTitle(), postRequest.getWriter(), postRequest.getContent());
        return resultId
                .map(id -> ResponseEntity.created(URI.create("http://localhost:8080/board/" + id)).build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        Optional<String> result = boardRepositoryImpl.delete(id);
        return result
                .map(msg -> ResponseEntity.accepted().body(msg))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
