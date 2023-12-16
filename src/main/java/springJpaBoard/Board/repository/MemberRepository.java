package springJpaBoard.Board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByName(String name);

    // name만 선링
    Page<Member> findByNameContaining(String keyword, Pageable pageable);

    // gender만 선택한 경우
    Page<Member> findByGender(GenderStatus gender, Pageable pageable);

    // name과 gender을 모두 입력한 경우
    Page<Member> findByNameContainingAndGender(String name, GenderStatus gender, Pageable pageable);

    /* loginId로 Member 찾아오기 */
    Optional<Member> findByLoginId(String loginId);
}

