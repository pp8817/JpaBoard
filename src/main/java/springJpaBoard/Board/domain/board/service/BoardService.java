package springJpaBoard.Board.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.board.exception.BoardNotFoundException;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.board.repository.BoardRepository;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;

import static springJpaBoard.Board.domain.board.dto.BoardDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(CreateBoardRequest boardRequestDTO, Long memberId) {

        Board board = boardRequestDTO.toEntity();
        //엔티티 조회
        Member member = memberService.findOne(memberId);

        //연관 관계 생성
        board.setMember(member);

        boardRepository.save(board);

        return board.getId();
    }

    /**
     * 게시글 조회
     * Search
     */

    /* 게시글 전체 조회 */
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /* 제목만 검색 */
    public Page<Board> searchTitle(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(keyword, pageable);
    }

    /* 제목, 성별 검색 */
    public Page<Board> searchAll(String keyword, String gender, Pageable pageable) {
        return boardRepository.findByTitleContainingAndMember_Gender(keyword, gender, pageable);
    }

    /**
     * 게시글 단건 조회
     */
    public Board findOne(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public ModifyBoardResponse update(Board board, ModifyBoardRequest boardDto) {
        /*
        Dirty Checking 발생
         */
        board.update(
                boardDto.title(),
                boardDto.content()
        );

        return ModifyBoardResponse.of(board);
    }

    /**
     * 게시글 삭제
     * @Transactional: 특정 실행 단위에서 오류 발생시 전체 실행 내용을 롤백해주는 기능
     */
    @Transactional
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    /**
     * 조회수 업데이트
     */
    @Transactional
    public void updateView(Long boardId) {
        boardRepository.updateView(boardId);
    }
}
