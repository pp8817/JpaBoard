package springJpaBoard.Board.domain.like.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.member.model.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // 연관 관계의 주인
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Like(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
