package springJpaBoard.Board.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springJpaBoard.Board.domain.Comment;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl {

    private final EntityManager em;

    /**
     * 댓글 작성
     */
    public void insert(Comment comment) {
        em.persist(comment);
    }

    /**
     * 댓글 모두 조회, 특정 게시물에 맞는 데이터만
     */

    /**
     * 댓글 삭제
     */
}
