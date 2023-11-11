package springJpaBoard.Board.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter //추후에 Getter 모두 없앨 예정
@Setter
public class Board {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime boardDateTime;

}
