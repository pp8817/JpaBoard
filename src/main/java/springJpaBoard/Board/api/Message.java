package springJpaBoard.Board.api;

import lombok.Data;

@Data
public class Message {
    private StatusEnum status = StatusEnum.BAD_REQUEST;
    private String message;
    private Object data;

    public Message(StatusEnum status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Message(StatusEnum status, String message) {
        this.status = status;
        this.message = message;
    }

    public Message() {
    }
}
