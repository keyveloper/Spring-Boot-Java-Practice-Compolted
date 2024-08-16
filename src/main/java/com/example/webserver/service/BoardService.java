package com.example.webserver.service;

import com.example.webserver.dto.GetBoardCommentDto;
import com.example.webserver.dto.GetBoardResultDto;
import com.example.webserver.dto.PostBoardResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostBoardStatus;
import com.example.webserver.repository.BoardRepository;
import com.google.common.base.Function;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final Function<BoardEntity, GetBoardResultDto> boardToResultDto = this::convertToResultDto;
    private final Function<CommentEntity, GetBoardCommentDto> commentToDto = this::convertCommentToDto

    public List<GetBoardResultDto> findAll() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.stream()
                .map(boardToResultDto)
                .collect(Collectors.toList());
    }

    public Optional<GetBoardResultDto> findById(long id) {
        return boardRepository.findById(id)
                .map(boardToResultDto);
        // 비어 있는 경우 map 메서드 적용이 안된다.
    }

    public PostBoardResultDto putBoard(String title, String writer, String textContent) {
        if (title != null && writer != null && textContent != null) {
            BoardEntity board = BoardEntity.builder()
                    .title(title).writer(writer)
                    .textContent(textContent)
                    .writingTime(LocalDateTime.now())
                    .readingCount(0)
                    .build();
            try {
                boardRepository.save(board);
                return PostBoardResultDto.builder()
                        .postBoardStatus(PostBoardStatus.Ok)
                        .id(board.getId()).writer(board.getWriter())
                        .writingTime(board.getWritingTime()).build();
            } catch (RuntimeException e) {
                throw new RuntimeException("Failed to save board", e);
            }
        }

        return PostBoardResultDto.builder()
                .postBoardStatus(PostBoardStatus.Failed)
                .id(-1)
                .writer(null)
                .writingTime(null)
                .build();
    }


    public Optional<String> deleteBoard(Long id) {
        if (boardRepository.existsById(id)) {
            boardRepository.deleteById(id);
            return Optional.of("\"Board Successfully deleted!! board id: \" + id");
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<String> updateBoard(long id, String content) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(id);
        if (boardOpt.isPresent()) {
            BoardEntity board = boardOpt.get();
            board.setTextContent(content);
            boardRepository.save(board);
            return Optional.of("new content updated successfully " + id);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<String> addReadingCount(long id) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(id);
        if (boardOpt.isPresent()) {
            BoardEntity board = boardOpt.get();
            board.setReadingCount(board.getReadingCount() + 1);
            boardRepository.save(board);
            return Optional.of("count added successfully " + board.getId());
        } else {
            return Optional.empty();
        }
    }

    public List<GetBoardResultDto> findByWriterOrContentLike(String writer, String textContent) {
        if (writer != null && textContent != null) {
            return boardRepository.findByWriterAndTextContentLike(writer, textContent).stream()
                    .map(boardToResultDto)
                    .collect(Collectors.toList());
        } else if (writer != null) {
            return boardRepository.findByWriterLike(writer).stream()
                    .map(boardToResultDto)
                    .collect(Collectors.toList());
        } else if (textContent != null) {
            return boardRepository.findByWriterLike(textContent).stream()
                    .map(boardToResultDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<GetBoardResultDto> findByWriterLikeDsl(String writer) {
        return boardRepository.findByWriterLikeDsl(writer)
                .stream()
                .map(boardToResultDto)
                .collect(Collectors.toList());
    }

    private GetBoardCommentDto convertCommentToDto(CommentEntity comment) {
        return GetBoardCommentDto.builder()
                .id(comment.getId())
                .writer(comment.getWriter())
                .writingTime(comment.getWritingTime())
                .textContent(comment.getTextContent())
                .build();
    }

    private GetBoardResultDto convertToResultDto(BoardEntity board) {
        return GetBoardResultDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .writingTime(board.getWritingTime())
                .readingCount(board.getReadingCount())
                .textContent(board.getTextContent())
                .comments(board.getComments().
                        stream()
                        .map(commentToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
