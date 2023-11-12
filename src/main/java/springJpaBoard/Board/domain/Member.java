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

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();


}
