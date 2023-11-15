package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.BoardRepositoryImpl;
import springJpaBoard.Board.repository.MemberRepositoryImpl;
import springJpaBoard.Board.service.dto.UpdateBoardDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepositoryImpl boardRepository;
    private final MemberRepositoryImpl memberRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(Board board, Long memberId) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        //연관 관계 생성
        board.setMember(member);

        boardRepository.write(board);

        return board.getId();
    }

    /**
     * 게시글 목록 조회
     */
    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    /**
     * 게시글 단건 조회
     */
    public Board findOne(Long boardId) {
        return boardRepository.findOne(boardId);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void update(UpdateBoardDto boardDto) {
        Board findBoard = boardRepository.findOne(boardDto.getId());

        /*
        Dirty Checking 발생
         */
        findBoard.editBoard(boardDto);
    }

    /**
     * 게시글 삭제
     */

}
