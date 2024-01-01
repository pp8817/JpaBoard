package springJpaBoard.Board.api.apiRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberApiRepository {
    private EntityManager em;

//    public Member findMemberWithBoards(Long memberId) {
//        return em.createQuery(
//                "select m from Member m where m.id = :memberId " +
//                        "join ", Member.class)
//                .setParameter("memberId", memberId)
//                .getSingleResult();
//    }
}
