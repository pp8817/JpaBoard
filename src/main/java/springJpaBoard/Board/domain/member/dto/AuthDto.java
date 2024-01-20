package springJpaBoard.Board.domain.member.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

public class AuthDto {
    @Builder
    public record LoginRequest(
            @NotBlank(message = "Id를 입력해주세요")
            String loginId,
            @NotBlank(message = "password를 입력해주세요")
            String password
    ) {

    }

}
