package springJpaBoard.Board.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import springJpaBoard.Board.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글 번호에 해당하는 댓글 목록 조회 및 페이징 처리
    Page<Comment> findAllByBno(Long bno, Pageable pageable);
}
