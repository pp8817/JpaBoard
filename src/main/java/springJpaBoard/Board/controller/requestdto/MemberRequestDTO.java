package springJpaBoard.Board.controller.requestdto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberRequestDTO {
    private Long id;

    @NotBlank(message = "Id를 입력해주세요", groups = {SaveCheck.class, LoginCheck.class})
//    @NotBlank
    private String loginId;

    @NotBlank(message = "password를 입력해주세요", groups = {SaveCheck.class, LoginCheck.class})
    private String password;

    /**
     * name 의 NotEmpty 조건이 모든 request, response 에 필요하지 않음에도 불필요하게 사용될 수 있으며,
     * 만약 각 API 의 request 와 response 에 맞추기 위해 domain 이 수정되서는 안되기 떄문이다.
     따라서 각 DTO에 필요한 데이터만 정의 되어야한다.

     "@NotBlank" 는 null 과 "" 과 " " 모두 허용하지 않는다.
     */
    @NotBlank(message = "회원 이름은 필수입니다.", groups = {SaveCheck.class, UpdateCheck.class})
    @Size(min = 1, max = 10, message = "회원 이름은 1~10자 사이입니다.", groups = {SaveCheck.class, UpdateCheck.class})
    private String name;

    private String gender;

//    private MemberStatus memberStatus;

    private String city;
    private String street;
    private String zipcode;

    public void createForm(Long id, String name, String gender, String city, String street, String zipcode) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
