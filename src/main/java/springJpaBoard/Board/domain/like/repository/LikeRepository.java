package springJpaBoard.Board.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.like.model.Like;
import springJpaBoard.Board.domain.member.model.Member;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndBoard(Member member, Board board);

    void deleteByMemberAndBoard(Member member, Board board);
}
