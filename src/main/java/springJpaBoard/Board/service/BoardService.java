package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.repository.BoardRepositoryImpl;
import springJpaBoard.Board.repository.dto.UpdateBoardDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepositoryImpl boardRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(Board board) {
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
    public void update(Long id, UpdateBoardDto boardDto) {
        Board findBoard = boardRepository.findOne(id);

        /*
        Dirty Checking 발생
         */
        findBoard.editBoard(boardDto.getTitle(), boardDto.getContent());
    }

    /**
     * 게시글 삭제
     */

}
