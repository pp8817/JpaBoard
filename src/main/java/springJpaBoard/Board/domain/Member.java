package springJpaBoard.Board.domain;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.service.dto.UpdateMemberDto;


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

    private String name;

    private GenderStatus gender;

//    private MemberStatus memberStatus;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") //양방향 연관관계 지정
    private List<Board> boardList = new ArrayList<>();

    public void createMember(String name, GenderStatus gender, Address address) {
        this.name = name;
        this.gender = gender;
        this.address = address;
    }

    /*
    회원 수정, Dirty Checking 발생(업데이트 쿼리가 자동으로 나감)
    Setter를 사용하지 않기 위해 수정 메서드를 만듦
     */
    public void editMember(UpdateMemberDto memberDto) {
        this.name = memberDto.getName();
        this.address = memberDto.getAddress();
    }

}
