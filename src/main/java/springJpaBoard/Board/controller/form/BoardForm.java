package springJpaBoard.Board.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
/**
 * 추후 변수로 Member를 추가할 예정 - 로그인 기능과 멤버를 구현했을때
 * 로그인 기능을 구현한 뒤에는 writer가 사라지고 연관관계에 있는 member의 name으로 대체
 */
public class BoardForm {

    private Long id;

    @NotBlank(message = "작성자는 필수입니다.")
    @Size(min = 1, max = 15, message = "1~15자 사이만 가능합니다.")
    private String writer;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 1, max = 50, message = "제목의 길이는 1~50자 사이입니다.")
    private String title;

//    @NotEmpty(message = "게시물의 내용을 작성 해주세요.")
    private String content;

    public void createForm(Long id, String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

}
