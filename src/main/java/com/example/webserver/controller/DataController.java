package com.example.webserver.controller;

import com.example.webserver.dto.RequestBoard;
import com.example.webserver.dto.RequestComment;
import com.example.webserver.dto.CommentResponse;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.service.BulletinService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class DataController {
    private final BulletinService bulletinService;

    @GetMapping("/boardAll")
    public ResponseEntity<List<BoardEntity>> getAllBoardEntity() {
        return bulletinService.getAllBoard()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardEntity> getBoard(@PathVariable("id") Long id) {
        return bulletinService.findBoard(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<Object> postBoard(@RequestBody RequestBoard request) {
        return bulletinService.putBoard(request.getTitle(), request.getWriter(), request.getContent())
                .map(id -> ResponseEntity.created(URI.create("http://localhost:8080/board/" + id)).build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        return bulletinService.deleteBoard(id)
                .map(msg -> ResponseEntity.accepted().body(msg))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentEntity> findCommentById(@PathVariable Long id) {
        return bulletinService.findCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/commentAll/{boardId}")
    public ResponseEntity<List<CommentEntity>> findAllComment(@PathVariable Long boardId) {
        return bulletinService.findAllComment(boardId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> postComment(@RequestBody RequestComment request) {
        log.info("request: {}", request);
        return bulletinService.putComment(request.getBoardId(), request.getWriter(), request.getTextContent())
                .map(response -> ResponseEntity.created(URI.create("http://localhost:8080/comment/"
                        + response.getCommentEntity().getCommentId())).body(response))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long id) {
        return bulletinService.deleteComment(id)
                .map(response -> ResponseEntity.accepted().body(response))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
