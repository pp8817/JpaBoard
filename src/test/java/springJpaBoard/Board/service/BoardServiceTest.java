package springJpaBoard.Board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.BoardRepositoryImpl;
import springJpaBoard.Board.service.dto.UpdateBoardDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest //junit4의 @RunWith(SpringRunner.class)이 속해있음
@Transactional
class BoardServiceTest {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepositoryImpl boardRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 게시물_작성() throws Exception {
        //given
        Board board = new Board();
        board.createBoard("JPA", "content", "Park", LocalDateTime.now());

        Member member = new Member();
        Address address = new Address("부산", "광안리", "123123");
        member.createMember("fff", "여성", address);
        em.persist(member);

        //when
        Long savedId = boardService.write(board, member.getId());

        //then
        assertEquals(board, boardRepository.findOne(savedId));
    }

    @Test
    public void 게시물_수정() throws Exception {
        //given
        Board board = new Board();
        board.createBoard("JPA", "content", "Park", LocalDateTime.now());

        Member member = new Member();
        Address address = new Address("부산", "광안리", "123123");
        member.createMember("mwadaw", "여성", address);
        em.persist(member);

        Long savedId = boardService.write(board, member.getId());

        //when
        UpdateBoardDto boardDto = new UpdateBoardDto(savedId, "Spring", "SangMin", LocalDateTime.now());

        boardService.update(boardDto);
        Board findBoard = boardService.findOne(savedId);

        //then
        assertEquals(findBoard.getId(), boardDto.getId());
        assertEquals(findBoard.getTitle(), boardDto.getTitle());
        assertEquals(findBoard.getContent(), boardDto.getContent());
    }
}