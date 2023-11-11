package springJpaBoard.Board.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.repository.dto.updateBoard;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final EntityManager em;

    @Override
    public void write(Board board) {
        em.persist(board);
    }

    @Override
    public Board findOne(Long boardId) {
        em.find(Board.class, boardId);
        return null;
    }

    @Override
    public List<Board> findAll() {
        List<Board> boardList = em.createQuery("select b from Board b", Board.class)
                .getResultList();
        return boardList;
    }

    @Override
    public void update(Long boardId, updateBoard updateBoard) {
        Board board = em.find(Board.class, boardId);

    }

    @Override
    public void delete(Long boardId) {

    }

    @Override
    public int count() {
        return 0;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
