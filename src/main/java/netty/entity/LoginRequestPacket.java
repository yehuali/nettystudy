package netty.entity;


import lombok.Data;

import static netty.entity.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {
    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }

    public int getVersion() {
        return 0;
    }


}