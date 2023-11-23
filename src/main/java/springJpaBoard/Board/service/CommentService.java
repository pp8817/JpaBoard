package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.repository.CommentRepositoryImpl;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardService boardService;
    private final CommentRepositoryImpl commentRepository;

    @Transactional
    public void save(Comment comment) {
        commentRepository.insert(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.delete(id);
    }
}
