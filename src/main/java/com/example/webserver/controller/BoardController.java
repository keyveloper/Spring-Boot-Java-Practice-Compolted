package com.example.webserver.controller;

import com.example.webserver.dto.*;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.enums.PostBoardStatus;
import com.example.webserver.service.BoardService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/boardAll")
    public ResponseEntity<List<BoardEntity>> getAllBoardEntity() {
        return boardService.getAllBoard()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardEntity> getBoard(@PathVariable("id") Long id) {
        return boardService.findBoard(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<PostBoardResponseDto> postBoard(@RequestBody PostBoardRequestDto request) {
        try {
            PostBoardResultDto postBoardResultDto = boardService.putBoard(
                    request.getTitle(), request.getWriter(), request.getTextContent());

            if (postBoardResultDto.getPostBoardStatus() == PostBoardStatus.Ok) {
                return ResponseEntity.ok().body(
                        PostBoardResponseDto.builder()
                                .message(PostBoardStatus.Ok.getMessage())
                                .id(postBoardResultDto.getId())
                                .writer(postBoardResultDto.getWriter())
                                .writingDateTime(postBoardResultDto.getWritingTime())
                                .build()
                );
            } else {
                return ResponseEntity.badRequest().body(
                        PostBoardResponseDto.builder()
                                .message(PostBoardStatus.Failed.getMessage())
                                .id(-1)
                                .writer(null)
                                .writingDateTime(null)
                                .build()
                );
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to save board", e);
        }
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        return boardService.deleteBoard(id)
                .map(msg -> ResponseEntity.accepted().body(msg))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/board/contain-board")
    public ResponseEntity<List<GetBoardResponseDto>> getBoardContainWriter(
            @PathVariable("writer") String writer,
            @PathVariable("textContent") String textContent) {
        return boardService.findBoardByContaining(writer, textContent)
                .map(resultDtos -> resultDtos.stream()
                        .map(this::convertToResponseDto)
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/board/contain-comment")
    public ResponseEntity<List<GetBoardResponseDto>> geBoardContainComment(
            @PathVariable("writer") String writer,
            @PathVariable("textContent") String content) {
        return boardService.findBoardByContainingComment(writer, content)
                .map(resultDtos -> resultDtos.stream()
                        .map(this::convertToResponseDto)
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping()
    private GetBoardResponseDto convertToResponseDto(GetBoardResultDto resultDto) {
        return GetBoardResponseDto.builder()
                .title(resultDto.getTitle())
                .comments(resultDto.getComments())
                .id(resultDto.getId())
                .textContent(resultDto.getTextContent())
                .writingTime(resultDto.getWritingTime())
                .readingCount(resultDto.getReadingCount())
                .writer(resultDto.getWriter())
                .build();
    }
}
