package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.handle.FirstClientHandler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final int MAX_RETRY = 10;

    public static void main(String[] args) {
        /**
         * option() :给连接设置一些 TCP 底层相关的属性
         * --->ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
         *     ChannelOption.SO_KEEPALIVE 表示是否开启 TCP 底层心跳机制，true 为开启
         */
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });

        // 4.建立连接
        connect(bootstrap, "127.0.0.1", 8080, MAX_RETRY);
    }

    //失败重连
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔(每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接)
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                /**
                 * bootstrap.config() 这个方法返回的是 BootstrapConfig
                 * --->对 Bootstrap 配置参数的抽象
                 * 1.bootstrap.config().group() 返回线程模型 workerGroup
                 * 2.调 workerGroup 的 schedule 方法即可实现定时任务逻辑
                 */
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }
}
