package com.example.webserver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Optional<List<BoardEntity>> getAllBoard() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.isEmpty() ? Optional.empty() : Optional.of(boards);
    }

    public Optional<Long> put(String title, String writer, String content) {
        BoardEntity board = new BoardEntity();
        board.setBoardTitle(title);
        board.setBoardWriter(writer);
        board.setBoardContent(content);
        board.setBoardDate(LocalDateTime.now());
        board.setBoardCount(0);
        boardRepository.save(board);
        return Optional.of(board.getBoardId());
    }

    public Optional<BoardEntity> get(long id) {
        return boardRepository.findById(id);
    }

    public Optional<String> delete(Long id) {
        if (boardRepository.existsById(id)) {
            boardRepository.deleteById(id);
            return Optional.of("\"Board Successfully deleted!! board id: \" + id");
        } else {
            return Optional.empty();
        }
    }


    public Optional<String> update(long id, String content) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(id);
        if (boardOpt.isPresent()) {
            BoardEntity board = boardOpt.get();
            board.setBoardContent(content);
            boardRepository.save(board);
            return Optional.of("new content updated successfully " + id);
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> addCount(long id) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(id);
        if (boardOpt.isPresent()) {
            BoardEntity board = boardOpt.get();
            board.setBoardCount(board.getBoardCount() + 1);
            boardRepository.save(board);
            return Optional.of("count added successfully " + board.getBoardId());
        } else {
            return Optional.empty();
        }
    }

    public Optional<List<CommentEntity>> getAllComment(long boardId) {
        List<CommentEntity> comments = commentRepository.findByBoardBoardId(boardId);
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }

}
