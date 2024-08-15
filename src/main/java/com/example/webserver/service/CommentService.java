package com.example.webserver.service;

import com.example.webserver.dto.CustomCriteria;
import com.example.webserver.dto.GetCommentResultDto;
import com.example.webserver.dto.PostCommentResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostCommentStatus;
import com.example.webserver.repository.BoardRepository;
import com.example.webserver.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    public List<GetCommentResultDto> findByBoardId(long boardId) {
        return commentRepository.findByBoardId(boardId)
                .stream()
                .map(this::convertToGetCommentResultDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public PostCommentResultDto putComment(long boardId, String writer, String textContent) {
        try {
            Optional<BoardEntity> board = boardRepository.findById(boardId);
            if (board.isEmpty()) {
                return PostCommentResultDto.builder()
                        .postCommentStatus(PostCommentStatus.Failed)
                        .boardId(-1)
                        .commentId(-1)
                        .writer(null)
                        .writingTime(null)
                        .build();
            } else {
                CommentEntity comment = CommentEntity.builder()
                        .board(board.get())
                        .writer(writer)
                        .writingTime(LocalDateTime.now())
                        .textContent(textContent)
                        .build();
                commentRepository.save(comment);
                return PostCommentResultDto.builder()
                        .postCommentStatus(PostCommentStatus.Ok)
                        .boardId(board.get().getId())
                        .commentId(comment.getId())
                        .writer(comment.getWriter())
                        .writingTime(comment.getWritingTime())
                        .build();
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save comment data", e);
        }
    }

    @Transactional
    public String deleteComment(long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return "commentId: " + commentId + "was deleted";
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Comment not found with id" + commentId, 1);
        }
    }

    @Transactional
    public List<GetCommentResultDto> findByBoardWriterLike(String writer) {
        return commentRepository.findByBoardWriterLike(writer)
                .stream()
                .map(this::convertToGetCommentResultDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<GetCommentResultDto> findByCustomCriteria(CustomCriteria customCriteria) {
        List<CommentEntity> result = commentRepository.findByCustomCriteria(customCriteria);
        log.info("result -> {}", result.toString());
        return commentRepository.findByCustomCriteria(customCriteria)
                .stream()
                .map(this::convertToGetCommentResultDto)
                .collect(Collectors.toList());
    }

    private GetCommentResultDto convertToGetCommentResultDto(CommentEntity comment) {
        return GetCommentResultDto.builder()
                .id(comment.getId())
                .boardId(comment.getBoard().getId())
                .writer(comment.getWriter())
                .writingTime(comment.getWritingTime())
                .textContent(comment.getTextContent())
                .build();
    }
}
