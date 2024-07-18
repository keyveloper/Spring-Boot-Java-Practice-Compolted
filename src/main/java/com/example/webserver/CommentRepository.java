package com.example.webserver;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<List<CommentEntity>> getAllComment();

    Optional<String> write(String writer, String content);

    Optional<String> delete(long id);
}
