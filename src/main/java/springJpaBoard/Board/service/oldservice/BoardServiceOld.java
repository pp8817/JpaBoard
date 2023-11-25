package springJpaBoard.Board.service.oldservice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.controller.requestdto.BoardForm;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.BoardRepository;
import springJpaBoard.Board.repository.oldRepository.BoardRepositoryImplOld;
import springJpaBoard.Board.repository.oldRepository.BoardRepositoryOld;
import springJpaBoard.Board.repository.oldRepository.MemberRepositoryImplOld;
import springJpaBoard.Board.repository.search.BoardSearch;

import java.util.List;

//@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceOld {

    private final BoardRepositoryImplOld boardRepositoryImplOld;
    private final MemberRepositoryImplOld memberRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(Board board, Long memberId) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        //연관 관계 생성
        board.setMember(member);

        boardRepositoryImplOld.write(board);

        return board.getId();
    }

    /**
     * 게시글 목록 조회(엔티티 그대로 반환), controller에서 DTO로 변환 필요
     */
    public List<Board> findBoards() {
        return boardRepositoryImplOld.findAll();
    }



    /**
     * 게시글 목록 조회(페이징 + 컬렉션 엔티티 조회), 마찬가지로 controller에서 DTO로 변환 필요
     * or 별도의 QueryRepository를 만들어서 JPA에서 DTO 직접 조회
     *  - JPA에서 직접 DTO를 조회하면서 원하는 데이터만 셀렉트, 최적화 가능
     */
    public List<Board> findBoardsMember(int offset, int limit) {
        return boardRepositoryImplOld.findAllWithBoardMember(offset, limit);
    }

    /**
     * 게시글 목록 조회(검색) - 검색 기능이 포함된 게시글 목록 조회
     * controller에서 DTO로 변환 필요
     */
    public List<Board> findBoardSearch(BoardSearch boardSearch) {
        return boardRepositoryImplOld.findAll2(boardSearch);
    }

    /**
     * 게시글 단건 조회
     */
    public Board findOne(Long boardId) {
        return boardRepositoryImplOld.findOne(boardId);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void update(Long id, BoardForm boardDto) {
        Board findBoard = boardRepositoryImplOld.findOne(id);
        /*
        Dirty Checking 발생
         */
        findBoard.editBoard(boardDto);
    }

    /**
     * 게시글 삭제
     * @Transactional: 특정 실행 단위에서 오류 발생시 전체 실행 내용을 롤백해주는 기능
     */
    @Transactional
    public void delete(Long boardId) {
        boardRepositoryImplOld.delete(boardId);
    }

    /**
     * 조회수 업데이트
     */
    @Transactional
    public void updateView(Long boardId) {
        boardRepositoryImplOld.updateView(boardId);
    }
}
