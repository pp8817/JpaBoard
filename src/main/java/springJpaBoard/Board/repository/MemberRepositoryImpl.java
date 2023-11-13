package springJpaBoard.Board.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.dto.UpdateBoardDto;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    private final EntityManager em;

    /**
     * 회원 저장
     */
    @Override
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * 회원 단건 조회
     */
    @Override
    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    /**
     * 회원 전체 조회
     */
    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * 이름으로 회원 조회
     * Parameter Binding 사용
     */
    @Override
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

//    /**
//     * 회원 수정, 추후 service에 작성, Transactional
//     */
//    @Override
//    public void update(Long memberId, UpdateBoardDto updateMember) {
//    }

    /**
     * 회원 삭제
     */
    @Override
    public void delete(Long memberId) {

    }
}
