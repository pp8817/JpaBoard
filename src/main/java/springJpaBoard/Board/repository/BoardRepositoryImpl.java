package springJpaBoard.Board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.QBoard;
import springJpaBoard.Board.domain.QMember;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final EntityManager em;

    /**
     * 게시물 작성
     */
    @Override
    public void write(Board board) {
        em.persist(board);
    }

    /**
     * 게시물 단건 조회
     */
    @Override
    public Board findOne(Long boardId) {
        return em.find(Board.class, boardId);
    }

    /**
     * 게시물 모두 조회
     * 게시물을 모두 조회하는 경우 게시판에 게시물 목록을 보여주기 위한 용도이므로 추후에 DTO를 만들어서 적용
     Q: DTO를 Result로 감싸야 하나?
     A: 내 생각에는 그럴 필요가 없음. Result로 감싸주는 이유는 api를 뿌릴 때 이를 사용하는 다른 사용자가 데이터를 추가하는 등의 작업을 할 때
     편의성을 위한 것임. api를 만들 때만 Result로 감싸주면 될 것이라고 생각함.
     */
    @Override
    public List<Board> findAll() {
        List<Board> boardList = em.createQuery("select b from Board b", Board.class)
                .getResultList();

        return boardList;
    }

    /**
     * 페이징 + 컬렉션 엔티티 조회
     *
     * ToOne 관계인 Member는 Fetch join을 한다.
     * ToOne 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.
     * 컬렉션은 지연 로딩으로 조회한다.
     *
     * 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size 적용
     */
    public List<Board> findAllWithBoardMember(int offset, int limit) {
        return em.createQuery("select b from Board b " +
                "join fetch b.member m ", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 검색 기능 포함
     */
    public List<Board> findAll2(BoardSearch boardSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        QMember member = QMember.member;

        return query.select(board)
                .from(board)
                .join(board.member, member)
                .where(genderEq(boardSearch.getMemberGender()),
                        titleLike(boardSearch.getBoardTitle()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression genderEq(String gender) {
        if (!StringUtils.hasText(gender)) {
            return null;
        }
        return QMember.member.gender.like(gender);
    }

    private BooleanExpression titleLike(String titleCond) {
        if (!StringUtils.hasText(titleCond)) {
            return null;
        }
        return QBoard.board.title.like(titleCond);
    }


    /**
     * 게시글 삭제
     */
    @Override
    public void delete(Long boardId) {
        Board board = em.find(Board.class, boardId);
        em.remove(board);
    }

    @Override
    public int count() {
        return 0;
    }

}
