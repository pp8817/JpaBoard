package springJpaBoard.Board.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static springJpaBoard.Board.controller.memberdto.MemberDto.ModifyMemberRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId; //로그인 ID
    
    private String password; //로그인 비밀번호

    private String name;

//    @Enumerated(EnumType.STRING)
    private String gender;

//    private MemberStatus memberStatus;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) //양방향 연관관계 지정
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade= CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    /*회원 가입을 위한 생성자*/
    @Builder
    public Member(Long id, String loginId, String password, String name, String gender, Address address) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.address = address;
    }
    /* TODO 위 방법 또는 아래 방법 중 고민 */
//    public static Member of(MemberRequestDTO memberRequestDTO) {
//        Address address = Address.builder()
//                .city(memberRequestDTO.getCity())
//                .street(memberRequestDTO.getStreet())
//                .zipcode(memberRequestDTO.getZipcode())
//                .build();
//
//        return Member.builder()
//                .name(memberRequestDTO.getName())
//                .gender(memberRequestDTO.getGender())
//                .loginId(memberRequestDTO.getLoginId())
//                .password(memberRequestDTO.getPassword())
//                .address(address)
//                .build();
//    }

    /*회원 수정*/
    @Builder
    public Member(String name, String gender, Address address) {
        this.name = name;
        this.gender = gender;
        this.address = address;
    }

    /*
        회원 수정, Dirty Checking 발생(업데이트 쿼리가 자동으로 나감)
        Setter를 사용하지 않기 위해 수정 메서드를 만듦
     */
    public void editMember(ModifyMemberRequest memberDto) {
        this.name = memberDto.name();
        this.gender = memberDto.gender();
        this.address = memberDto.address();
    }

}
