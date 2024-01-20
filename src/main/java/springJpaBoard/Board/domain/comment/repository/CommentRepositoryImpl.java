package springJpaBoard.Board.domain.comment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springJpaBoard.Board.domain.comment.model.Comment;

import javax.persistence.EntityManager;
import java.util.List;

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
    public List<Comment> list(Long bno) {
        List<Comment> comments = em.createQuery("select c from Comment c where c.bno = :bno", Comment.class)
                .setParameter("bno", bno)
                .getResultList();
        return comments;
    }

    /*
    댓글 단건 조회
     */
    public Comment findById(Long id) {
        return em.find(Comment.class, id);
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long commentId) {
        Comment comment = findById(commentId);
        em.remove(comment);
    }

}
