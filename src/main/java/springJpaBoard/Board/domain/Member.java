package springJpaBoard.Board.domain;

import lombok.Getter;
import springJpaBoard.Board.controller.requestdto.MemberRequestDTO;
import springJpaBoard.Board.domain.status.GenderStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @GeneratedValue
    @Id
    @Column(name = "member_id")
    private Long id;

    private String loginId; //로그인 ID
    
    private String password; //로그인 비밀번호

    private String name;

    @Enumerated(EnumType.STRING)
    private GenderStatus gender;

//    private MemberStatus memberStatus;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) //양방향 연관관계 지정
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade= CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    public void createMember(MemberRequestDTO memberRequestDTO, Address address) {
        this.name = memberRequestDTO.getName();
        this.gender = memberRequestDTO.getGender();
        this.loginId = memberRequestDTO.getLoginId();
        this.password = memberRequestDTO.getPassword();
        this.address = address;
    }

    /*
    회원 수정, Dirty Checking 발생(업데이트 쿼리가 자동으로 나감)
    Setter를 사용하지 않기 위해 수정 메서드를 만듦
     */
    public void editMember(MemberRequestDTO memberDto) {
        this.name = memberDto.getName();
        this.gender = memberDto.getGender();
        this.address.updateAddress(memberDto.getCity(), memberDto.getStreet(), memberDto.getZipcode());
    }

}
