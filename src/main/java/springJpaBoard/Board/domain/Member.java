package springJpaBoard.Board.domain;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @GeneratedValue
    @Id
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String gender;

    private MemberStatus memberStatus;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") //양방향 연관관계 지정
    private List<Board> boardList = new ArrayList<>();


}
