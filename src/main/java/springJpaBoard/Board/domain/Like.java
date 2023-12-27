package springJpaBoard.Board.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "likes")
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // 연관 관계의 주인
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")

    private Board board;

    public Like() {
    }

    public Like(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
