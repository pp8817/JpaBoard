package springJpaBoard.Board.global.Error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private StatusEnum status = StatusEnum.BAD_REQUEST;
    private String message;
    private Object data;


    public Message(StatusEnum status, String message) {
        this.status = status;
        this.message = message;
    }

    public Message() {
    }
}
