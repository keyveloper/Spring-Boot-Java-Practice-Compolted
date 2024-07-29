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
    private final BoardService boardService;

    @GetMapping("/boardAll")
    public ResponseEntity<List<BoardEntity>> getAllBoardEntity() {
        return boardService.getAllBoard()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardEntity> getBoard(@PathVariable("id") Long id) {
        return boardService.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<Object> postBoard(@RequestBody BoardPostRequest postRequest) {
        return boardService.put(postRequest.getTitle(), postRequest.getWriter(), postRequest.getContent())
                .map(id -> ResponseEntity.created(URI.create("http://localhost:8080/board/" + id)).build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        return boardService.delete(id)
                .map(msg -> ResponseEntity.accepted().body(msg))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/commentAll/{boardId}")
    public ResponseEntity<List<BoardEntity>> getAllComment(@PathVariable Long boardId) {
        return null;
    }

}
