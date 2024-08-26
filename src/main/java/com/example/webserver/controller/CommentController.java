package com.example.webserver.controller;

import com.example.webserver.dto.*;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostCommentStatus;
import com.example.webserver.enums.UpdateStatus;
import com.example.webserver.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Getter
public class CommentController {
    private final CommentService commentService;
    @GetMapping("/comments/{boardId}")
    public ResponseEntity<List<GetCommentResponseDto>> findByBoardId(@PathVariable Long boardId) {
        List<GetCommentResultDto> resultDtos = commentService.findByBoardId(boardId);
        return ResponseEntity.ok(resultDtos
                .stream()
                .map(this::convertGetResultToGetResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<GetCommentResponseDto> getById(@PathVariable Long id) {
        Optional<GetCommentResultDto> resultDto = commentService.findById(id);
        return resultDto.map(getBoardResultDto -> ResponseEntity.ok(convertGetResultToGetResponse(getBoardResultDto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<PostCommentResponseDto> postComment(@RequestBody PostCommentRequestDto request) {
        log.info("request: {}", request);
        PostCommentResultDto commentResultDto = commentService.putComment(
                request.getBoardId(), request.getWriter(), request.getTextContent());
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

    //update
    @PostMapping("/comment/update")
    public ResponseEntity<String> updateComment(
            @RequestParam long id,
            @RequestParam String textContent,
            @RequestParam String writer
    ) {
        UpdateResultDto resultDto = commentService.updateComment(
                UpdateRequestDto.builder()
                        .id(id)
                        .textContent(textContent)
                        .writer(writer)
                        .build()
        );
        if (resultDto.getUpdateStatus() == UpdateStatus.Ok) {
            return ResponseEntity.ok().body(resultDto.getUpdateStatus().getMessage());
        }
        return ResponseEntity.badRequest().body(resultDto.getUpdateStatus().getMessage());
    }

    @GetMapping("/comments/like/{boardWriter}")
    public ResponseEntity<List<GetCommentResponseDto>> getByBoarWriterLike(@PathVariable String boardWriter) {
        List<GetCommentResultDto> resultDtos = commentService.findByBoardWriterLike(boardWriter);
        return ResponseEntity.ok(resultDtos
                .stream()
                .map(this::convertGetResultToGetResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/comments/advanced-search-in-board")
    public ResponseEntity<List<GetCommentResponseDto>> getByAdvancedSearchInBoard(
            @RequestParam String writer,
            @RequestParam String notContainWriter,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Integer maxReads,
            @RequestParam Integer minReads
            ) {
        List<GetCommentResultDto> resultDtos = commentService.findByCustomCriteria(
                CustomCriteria.builder()
                        .writer(writer)
                        .notContainWriter(notContainWriter)
                        .startDate(startDate)
                        .endDate(endDate)
                        .maxReads(maxReads)
                        .minReads(minReads)
                        .build()
        );
        return ResponseEntity.ok(resultDtos
                .stream()
                .map(this::convertGetResultToGetResponse)
                .collect(Collectors.toList()));
    }

    private GetCommentResponseDto convertGetResultToGetResponse(GetCommentResultDto resultDto) {
        return GetCommentResponseDto.builder()
                .id(resultDto.getId())
                .boardId(resultDto.getBoardId())
                .writer(resultDto.getWriter())
                .writingTime(resultDto.getWritingTime())
                .lastModifiedTime(resultDto.getLastModifiedTime())
                .textContent(resultDto.getTextContent())
                .build();
    }
}