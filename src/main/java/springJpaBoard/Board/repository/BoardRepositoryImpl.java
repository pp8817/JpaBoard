package springJpaBoard.Board.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springJpaBoard.Board.domain.Board;

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
        em.find(Board.class, boardId);
        return null;
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

//    /**
//     * 게시글 수정
//     * 게시글 수정은 제목, 내용만 가능
//     * 추후 service로 이동, Transactional
//     */
//    @Override
//    public void update(Long boardId, UpdateBoardDto boardDto) {
//        Board board = em.find(Board.class, boardId);
//        /*
//        Dirty Checking 발생, 가능하다면 Setter는 사용하지 않는 방법으로 구현
//         */
//        board.editBoard(boardDto.getTitle(), boardDto.getContent());
//    }

    @Override
    public void delete(Long boardId) {

    }

    @Override
    public int count() {
        return 0;
    }

}
