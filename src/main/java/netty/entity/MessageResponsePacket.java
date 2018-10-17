package netty.entity;

import lombok.Data;

import static netty.entity.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {
    private String message;



    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
