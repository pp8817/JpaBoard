package springJpaBoard.Board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.repository.BoardRepositoryImpl;
import springJpaBoard.Board.service.dto.UpdateBoardDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest //junit4의 @RunWith(SpringRunner.class)이 속해있음
@Transactional
class BoardServiceTest {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepositoryImpl boardRepository;

    @Test
    public void 게시물_작성() throws Exception {
        //given
        Board board = new Board();
        board.createBoard("JPA", "content", "Park");

        //when
        Long savedId = boardService.write(board);

        //then
        assertEquals(board, boardRepository.findOne(savedId));
    }

    @Test
    public void 게시물_수정() throws Exception {
        //given
        Board board = new Board();
        board.createBoard("JPA", "content", "Park");
        Long savedId = boardService.write(board);

        //when
        UpdateBoardDto boardDto = new UpdateBoardDto(savedId, "Spring", "Sangmin");

        boardService.update(boardDto);
        Board findBoard = boardService.findOne(savedId);

        //then
        assertEquals(findBoard.getId(), boardDto.getId());
        assertEquals(findBoard.getTitle(), boardDto.getTitle());
        assertEquals(findBoard.getContent(), boardDto.getContent());
    }
}