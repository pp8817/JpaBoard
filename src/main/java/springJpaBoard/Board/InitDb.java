package springJpaBoard.Board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
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
            member.createMember("memberA", "Man", address);
            em.persist(member);

            Board board = new Board();
            board.createBoard("spring", "wadad", "Park", LocalDateTime.now());
            boardService.write(board, member.getId());
        }

        public void dbInit2() {

            Member member = new Member();
            Address address = new Address("부산", "광안리", "123123");
            member.createMember("memberB", "Woman", address);
            em.persist(member);

            Board board = new Board();
            board.createBoard("JPA", "pppaapp", "sangmin", LocalDateTime.now());
            boardService.write(board, member.getId());
        }

    }
}
