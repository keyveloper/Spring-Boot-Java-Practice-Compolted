package com.example.webserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Optional<List<BoardEntity>> getAllBoard();
    Optional<Long> put(String writer, String content);
    Optional<BoardEntity> get(long id);
    Optional<String> delete(long id);
    Optional<String> update(long id, String content);
    Optional<String> addCount(long id);

    Optional<List<CommentEntity>> getAllComment(long id);

}
