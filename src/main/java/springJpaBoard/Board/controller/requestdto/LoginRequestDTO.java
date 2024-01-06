package springJpaBoard.Board.controller.requestdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class LoginRequestDTO {
    @NotBlank(message = "Id를 입력해주세요", groups = {SaveCheck.class, LoginCheck.class})
//    @NotBlank
    private String loginId;

    @NotBlank(message = "password를 입력해주세요", groups = {SaveCheck.class, LoginCheck.class})
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
