package springJpaBoard.Board.api;

import lombok.Data;

@Data
public class Message {
    private StatusEnum status;
    private String message;
    private Object data;

    public Message(StatusEnum status, String message, Object data) {
        this.status = StatusEnum.BAD_REQUEST;
        this.message = message;
        this.data = data;
    }

    public Message() {
    }
}
