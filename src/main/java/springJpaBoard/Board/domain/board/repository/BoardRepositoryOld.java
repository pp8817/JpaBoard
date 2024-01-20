package springJpaBoard.Board.domain.board.repository;

import springJpaBoard.Board.domain.board.model.Board;

import java.util.List;

public interface BoardRepositoryOld {

    // 게시물 작성
    void write(Board board);

    // 게시물 조회
    Board findOne(Long boardId);

    // 게시물 목록
    List<Board> findAll();

    // 게시물 수정
//    void update(Long boardId, UpdateBoardDto updateBoard);

    // 게시뮬 삭제
    void delete(Long boardId);

    // 게시물 총 갯수
    int count();
}
