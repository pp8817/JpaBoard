package springJpaBoard.Board.domain.board.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import springJpaBoard.Board.domain.comment.model.Comment;
import springJpaBoard.Board.domain.like.model.Like;
import springJpaBoard.Board.domain.member.model.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate //업데이트 쿼리 최적화
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") //FK
    private Member member; //연관관계의 주인

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) //게시글이 삭제되면 댓글도 모두 삭제
    @OrderBy("id asc") //댓글 정렬
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    private String title;

    private String content;

    private String writer;

    private int view;

    private int commentCount;

    private int likes;

    private LocalDateTime boardDateTime;

    private LocalDateTime modifyDateTime;


    @Builder
    public Board(String title, String content, String writer, LocalDateTime localDateTime) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.boardDateTime = LocalDateTime.now();
    }

    /*
    게시글 수정, Dirty Checking 발생(업데이트 쿼리가 자동으로 나감)
    Setter를 사용하지 않기 위해 수정 메서드를 만듦
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifyDateTime = LocalDateTime.now();
    }

    public void addComment() {
        this.commentCount += 1;
    }

    public void decreaseComment() {
        this.commentCount -= 1;
    }

    public void addLike() {
        this.likes += 1;
    }

    public void decreaseLike() {
        this.likes -= 1;
    }


    /*
    연관관계 편의 메서드 - 위치는 핵심적으로 컨트롤하는 곳에 작성
     */
    public void setMember(Member member) {
        this.member = member;
        member.getBoardList().add(this);
    }

}
