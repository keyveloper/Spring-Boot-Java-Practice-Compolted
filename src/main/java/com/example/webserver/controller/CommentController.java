package com.example.webserver.controller;

import com.example.webserver.dto.*;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostCommentStatus;
import com.example.webserver.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentEntity> findCommentById(@PathVariable Long id) {
        return commentService.findCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/commentAll/{boardId}")
    public ResponseEntity<List<CommentEntity>> findAllComment(@PathVariable Long boardId) {
        return commentService.findAllComment(boardId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<PostCommentResponseDto> postComment(@RequestBody PostCommentRequestDto request) {
        log.info("request -> {}", request.toString());
        PostCommentResultDto commentResultDto = commentService.putComment(
                request.getBoardId(), request.getWriter(), request.getTextContent());
        log.info("resultDto -> {}", commentResultDto.toString());
        // status별로 Response 구분
        if (commentResultDto.getPostCommentStatus() == PostCommentStatus.Ok) {
            return ResponseEntity.ok().body(
                    PostCommentResponseDto.builder()
                            .boardId(commentResultDto.getBoardId())
                            .commentId(commentResultDto.getCommentId())
                            .writer(commentResultDto.getWriter())
                            .writingTime(commentResultDto.getWritingTime())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    PostCommentResponseDto.builder()
                            .boardId(-1)
                            .commentId(-1)
                            .writer(null)
                            .writingTime(null)
                            .build()
            );
        }
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        try {
            String commentDeleteMessage = commentService.deleteComment(id);
            return ResponseEntity.ok().body(commentDeleteMessage);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("can not delete comment");
        }
    }

    @GetMapping("/comment/advanced-search-in-board")
    public ResponseEntity<List<GetCommentResponseDto>> getBoardAdvancedSearch(
            @RequestParam String writer,
            @RequestParam String notContainWriter,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Integer minReads,
            @RequestParam Integer maxReads
    ) {
        return commentService.findByComplexCriteria(
                        GetBoardCriteriaRequest.builder()
                                .writer(writer)
                                .notContainWriter(notContainWriter)
                                .startDate(startDate)
                                .endDate(endDate)
                                .minReads(minReads)
                                .maxReads(maxReads)
                                .build())
                .map(comments -> comments.stream()  // Optional의 map을 사용하여 List<CommentEntity>를 처리
                        .map(this::commentToResponseDto)  // 각 CommentEntity를 GetCommentResponseDto로 변환
                        .collect(Collectors.toList()))  // List<GetCommentResponseDto>로 수집
                .map(ResponseEntity::ok)  // ResponseEntity.ok()로 변환
                .orElseGet(() -> ResponseEntity.notFound().build());  // 비어 있으면 404 Not Found 반환
    }

    private GetCommentResponseDto commentToResponseDto(CommentEntity comment) {
        return GetCommentResponseDto.builder()
                .writer(comment.getWriter())
                .writingTime(comment.getWritingTime())
                .boardId(comment.getBoard().getId())
                .id(comment.getId())
                .textContent(comment.getTextContent())
                .build();
    }
}