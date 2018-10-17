package netty.entity;

import lombok.Data;

import static netty.entity.Command.MESSAGE_REQUEST;
@Data
public class MessageRequestPacket extends Packet {
    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
