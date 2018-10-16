package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import netty.handle.FirstClientHandler;
import netty.handle.FirstServerHandler;

public class NettyServer {
    public static void main(String[] args) {


        //线程模型
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();//accept 新连接的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();//处理每一条连接的数据读写的线程组

        /**
         * NioSocketChannel NioServerSocketChannel :NIO 类型的连接的抽象
         * handler()和childHandler()方法对应起来
         * childHandler()用于指定处理新连接数据的读写处理逻辑
         * handler()用于指定在服务端启动过程中的一些逻辑（一般用不着）
         *
         * serverBootstrap.attr(AttributeKey.newInstance("serverName"), "nettyServer")
         * --->NioServerSocketChannel指定一些自定义属性  channel.attr()取出这个属性
         * serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
         * --->可以给每一条连接指定自定义属性 channel.attr()取出该属性
         * childOption()可以给每条连接设置一些TCP底层相关的属性
         *  --->ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制，true为开启
         *      ChannelOption.TCP_NODELAY表示是否开启Nagle算法，true表示关闭，false表示开启(要求高实时性，有数据发送时就马上发送，就关闭)
         * option() 给服务端channel设置一些属性
         *   -->ChannelOption.SO_BACKLOG,1024  :临时存放已完成三次握手的请求的队列的最大长度
         *
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)//  IO 模型
                .childHandler(new ChannelInitializer<NioSocketChannel>() {//定义后续每条连接的数据读写，业务处理逻辑
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });

        bind(serverBootstrap,8080);
    }

    /**
     *  自动绑定递增端口
     *  bind异步方法,调用之后是立即返回,给这个ChannelFuture添加一个监听器GenericFutureListener
     *   1000 开始到 1023
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }
}
