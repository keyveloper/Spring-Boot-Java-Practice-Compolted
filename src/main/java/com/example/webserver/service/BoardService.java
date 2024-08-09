package com.example.webserver.service;

import com.example.webserver.dto.GetBoardResultDto;
import com.example.webserver.dto.PostBoardResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.enums.PostBoardStatus;
import com.example.webserver.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Optional<List<BoardEntity>> getAllBoard() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.isEmpty() ? Optional.empty() : Optional.of(boards);
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

    public Optional<BoardEntity> findBoard(long id) {
        return boardRepository.findById(id);
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

    public Optional<List<GetBoardResultDto>> findBoardByContaining(String writer, String textContent) {
        if (writer != null && textContent != null) {
            List<GetBoardResultDto> boardDtos = boardRepository.findByContainingWriterAndText(writer, textContent)
                    .stream()
                    .map(this::convertResultDto)
                    .collect(Collectors.toList());

            if (boardDtos.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(boardDtos);
        } else if (writer != null) {
            List<GetBoardResultDto> boardDtos = boardRepository.findByContainingWriter(writer)
                    .stream()
                    .map(this::convertResultDto)
                    .collect(Collectors.toList());

            if (boardDtos.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(boardDtos);
        } else if (textContent != null){
            List<GetBoardResultDto> boardDtos = boardRepository.findByContainingTextContent(textContent)
                    .stream()
                    .map(this::convertResultDto)
                    .collect(Collectors.toList());

            if (boardDtos.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(boardDtos);

        }
        return Optional.empty();
    }


    public Optional<List<GetBoardResultDto>> findBoardByContainingComment(String writer, String textContent) {
        if (writer != null && textContent != null) {
            List<GetBoardResultDto> boardDtos = boardRepository
                    .findByContainingCommentWriterAndText(writer, textContent)
                    .stream()
                    .map(this::convertResultDto)
                    .collect(Collectors.toList());

            if (boardDtos.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(boardDtos);
        } else if (writer != null) {
            List<GetBoardResultDto> boardDtos = boardRepository
                    .findByContainingCommentWriter(writer)
                    .stream()
                    .map(this::convertResultDto)
                    .collect(Collectors.toList());

            if (boardDtos.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(boardDtos);
        } else if (textContent != null){
            List<GetBoardResultDto> boardDtos = boardRepository
                    .findByContainingCommentContent(textContent)
                    .stream()
                    .map(this::convertResultDto)
                    .collect(Collectors.toList());

            if (boardDtos.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(boardDtos);

        }
        return Optional.empty();
    }

    private GetBoardResultDto convertResultDto(BoardEntity board) {
        return GetBoardResultDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .readingCount(board.getReadingCount())
                .comments(board.getComments())
                .writingTime(board.getWritingTime())
                .textContent(board.getTextContent())
                .build();
    }
}
