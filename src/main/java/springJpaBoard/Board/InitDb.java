package springJpaBoard.Board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct

    public void init() {
        initService.dbInit1();
        initService.dbInit2();
        initService.dbInit3();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor

    static class InitService {

        private final MemberService memberService;
        private final BoardService boardService;
        private final EntityManager em;

        public void dbInit1() {

            Member member = new Member();
            Address address = new Address("서울", "강남", "1283");
            member.createMember("memberA", GenderStatus.남성, address);
            em.persist(member);

            Board board = new Board();
            board.createBoard("spring", "springboot", "Park", LocalDateTime.now());
            boardService.write(board, member.getId());
        }

        public void dbInit2() {

            Member member = new Member();
            Address address = new Address("부산", "광안리", "123123");
            member.createMember("memberB", GenderStatus.여성, address);
            em.persist(member);

            Board board = new Board();
            board.createBoard("JPA", "springDataJpa", "sangmin", LocalDateTime.now());
            boardService.write(board, member.getId());
        }

        public void dbInit3() {

            Member member = new Member();
            Address address = new Address("강원도", "강릉", "5423");
            member.createMember("memberC", GenderStatus.중성, address);
            em.persist(member);

            Board board = new Board();
            board.createBoard("객체지향", "객체지향의 사실과 오해", "김영한", LocalDateTime.now());
            boardService.write(board, member.getId());
        }

    }
}
