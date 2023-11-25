package springJpaBoard.Board.repository.Old;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.QMember;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.search.MemberSearch;

import javax.persistence.EntityManager;
import java.util.List;

//@Repository
@RequiredArgsConstructor
public class MemberRepositoryImplOld implements MemberRepositoryOld {

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
     * 검색 기능이 포함된 회원 조회
     */
    public List<Member> findAll2(MemberSearch memberSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember member = QMember.member;

        return query.select(member)
                .from(member)
                .where(nameLike(memberSearch.getMemberName()),
                        statusEq(memberSearch.getMemberGender()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String nameCond) {
        if (!StringUtils.hasText(nameCond)) {
            return null;
        }
        return QMember.member.name.like(nameCond);
    }

    private BooleanExpression statusEq(GenderStatus genderStatus) {
        if (genderStatus == null) {
            return null;
        }
        return QMember.member.gender.eq(genderStatus);
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
        em.remove(em.find(Member.class, memberId));
    }
}
