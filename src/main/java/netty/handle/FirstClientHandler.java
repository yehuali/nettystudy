package netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.encodeAnddecode.PacketCodeC;
import netty.entity.LoginRequestPacket;
import netty.entity.LoginResponsePacket;
import netty.util.LoginUtil;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

public class FirstClientHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    //户端连接建立成功之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 编码
//        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        // 2. 写数据
        ctx.channel().writeAndFlush(loginRequestPacket);
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        /**
         * 1. 获取二进制抽象 ByteBuf
         * ctx.alloc() 获取到一个 ByteBuf 的内存管理器
         * --->作用:分配一个 ByteBuf
         */
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，闪电侠!".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }
}
