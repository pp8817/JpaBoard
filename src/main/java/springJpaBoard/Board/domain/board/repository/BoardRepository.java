package springJpaBoard.Board.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import springJpaBoard.Board.domain.board.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    Page<Board> findByTitleContainingAndMember_Gender(String keyword, String gender, Pageable pageable);

    @Modifying
    @Query("UPDATE Board b SET b.view = b.view + 1 WHERE b.id = :id")
    void updateView(Long id);

}
