package springJpaBoard.Board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.controller.requestdto.BoardForm;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.Old.BoardRepositoryImplOld;
import springJpaBoard.Board.service.Old.BoardServiceOld;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest //junit4의 @RunWith(SpringRunner.class)이 속해있음
@Transactional
class BoardServiceTest {
    @Autowired
    BoardServiceOld boardServiceOld;

    @Autowired
    BoardRepositoryImplOld boardRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 게시물_작성() throws Exception {
        //given
        Board board = new Board();
        board.createBoard("JPA", "content", "Park", LocalDateTime.now());

        Member member = new Member();
        Address address = new Address("부산", "광안리", "123123");
        member.createMember("fff", GenderStatus.여성, address);
        em.persist(member);

        //when
        Long savedId = boardServiceOld.write(board, member.getId());

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
        member.createMember("mwadaw", GenderStatus.여성, address);
        em.persist(member);

        Long savedId = boardServiceOld.write(board, member.getId());

        BoardForm updateBoard = new BoardForm();
        updateBoard.setId(savedId);
        updateBoard.setTitle("Spring");
        updateBoard.setContent("SangMin");

        boardServiceOld.update(savedId, updateBoard);
        Board findBoard = boardServiceOld.findOne(savedId);

        //then
        assertEquals(findBoard.getId(), updateBoard.getId());
        assertEquals(findBoard.getTitle(), updateBoard.getTitle());
        assertEquals(findBoard.getContent(), updateBoard.getContent());
    }

    @Test
    public void 게시글_삭제() throws Exception {
        //given
        Board board = new Board();
        board.createBoard("JPA", "content", "Park", LocalDateTime.now());

        Member member = new Member();
        Address address = new Address("부산", "광안리", "123123");
        member.createMember("mwadaw", GenderStatus.여성, address);
        em.persist(member);

        Long savedId = boardServiceOld.write(board, member.getId());

        Long boardId = board.getId();

        //when
        boardServiceOld.delete(boardId);

        Board findBoard = boardServiceOld.findOne(boardId);
        //then
        assertEquals(findBoard, null);
    }
}