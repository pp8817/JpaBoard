package springJpaBoard.Board.controller.requestdto;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * 추후 변수로 Member를 추가할 예정 - 로그인 기능과 멤버를 구현했을때
 * 로그인 기능을 구현한 뒤에는 writer가 사라지고 연관관계에 있는 member의 name으로 대체
 */
public class BoardForm {

    private Long id;

    @NotBlank(message = "회원을 선택") //현재 작동 X
    private Member member;

    @NotBlank(message = "제목은 필수입니다.", groups = {SaveCheck.class, UpdateCheck.class})
    @Size(min = 1, max = 50, message = "제목의 길이는 1~50자 사이입니다.", groups = {SaveCheck.class, UpdateCheck.class})
    private String title;

    @NotBlank(message = "작성자는 필수입니다.", groups = {SaveCheck.class})
    @Size(min = 1, max = 30, message = "1~30자 사이만 가능합니다.", groups = {SaveCheck.class})
    private String writer;

//    @NotEmpty(message = "게시물의 내용을 작성 해주세요.")
    @Size(max = 300, message = "300자가 최대입니다.")
    private String content;

    private LocalDateTime modifyDateTime;

    private int recommend;


    public void createForm(Long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

}
