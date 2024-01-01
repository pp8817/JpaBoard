package springJpaBoard.Board.api.apirepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springJpaBoard.Board.domain.Board;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BoardApiRepository {
    private final EntityManager em;

    public Board findBoardWithMember(Long boardId) {
        return em.createQuery(
                "select b from Board b " +
                        "join fetch b.member m " +
                        "where b.id = :boardId", Board.class)
                .setParameter("boardId", boardId)
                .getSingleResult();
    }
}
