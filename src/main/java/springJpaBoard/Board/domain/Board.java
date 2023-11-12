package springJpaBoard.Board.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter //추후에 Getter 모두 없앨 예정
@Setter
public class Board {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private int id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") //FK
    private Member member; //연관관계의 주인

    private String title;

    private String content;

    private String writer;

    private LocalDateTime boardDateTime;

    /*
    연관관계 편의 메서드 - 위치는 핵심적으로 컨트롤하는 곳에 작성
     */
    public void setMember(Member member) {
        this.member = member;
        member.getBoardList().add(this);
    }

    /*
    게시글 수정, Dirty Checking 발생(업데이트 쿼리가 자동으로 나감)
    Setter를 사용하지 않기 위해 수정 메서드를 만듦
     */
    public void editBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
