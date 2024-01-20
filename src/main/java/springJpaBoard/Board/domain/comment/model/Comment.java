package springJpaBoard.Board.domain.comment.model;

import lombok.*;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.member.model.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
/**
 * 기능: 댓글을 달 회원을 선택하고 댓글을 달 수 있다.
 이름을 검증해서 글쓰이와 동일한 회원이 댓글을 다는 경우 이름은 '글쓴이',

 * 속성: id, content, createDateTime, idx(어떤 게시물에 댓글을 달았는지) writer?(회원 엔티티랑 연결해서 가져와서 등록?)
 * 연관관계: 회원과 1:N, 게시글과 1:N 둘 모두 연관관계의 주인은 테이블
 * 회원과는 (아직까지는) 단방향, 게시글과는 양방향

 고민할 점
 1. 회원 삭제 기능을 구현 시 댓글 또한 같이 삭제되도록 구현해야한다.
    - 연관관계로 엮고 CASCADE.ALL 기능을 사용하면 된다.
 */
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Long bno;

    private String writer; // 작성자

    private String content;  //댓글 내용

    private LocalDateTime createDateTime;  //작성 시간

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(Long bno, String writer, String content, LocalDateTime localDateTime) {
        this.bno = bno;
        this.writer = writer;
        this.content = content;
        this.createDateTime = LocalDateTime.now();
    }


    /*
    연관관계 편의 메서드
     */
    public void setBoard(Board board) {
        this.board = board;
        board.getCommentList().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getCommentList().add(this);
    }
}
