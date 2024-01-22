package springJpaBoard.Board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Like;
import springJpaBoard.Board.domain.Member;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndBoard(Member member, Board board);

    void deleteByMemberAndBoard(Member member, Board board);
}
