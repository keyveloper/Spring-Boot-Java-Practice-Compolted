package com.example.webserver.service;

import com.example.webserver.dto.GetBoardCriteriaRequest;
import com.example.webserver.dto.PostCommentResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.repository.BoardRepository;
import com.example.webserver.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Optional<CommentEntity> findCommentById(long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid comment id: " + id));
        return Optional.of(comment);
    }

    public Optional<List<CommentEntity>> findAllComment(long boardId) {
        List<CommentEntity> comments = commentRepository.findByBoardId(boardId);
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }

    @Transactional
    public PostCommentResultDto putComment(long boardId, String writer, String textContent) {
        try {
            Optional<BoardEntity> board = boardRepository.findById(boardId);
            if (board.isEmpty()) {
                return PostCommentResultDto.builder()
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
                return PostCommentResultDto.builder()
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
    public Optional<List<CommentEntity>> findByComplexCriteria(GetBoardCriteriaRequest criteria) {
        return Optional.of(commentRepository.findByComplexCriteria(criteria));
    }

}
