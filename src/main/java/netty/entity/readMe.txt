第一个字节为 1 表示这是一个登录指令,接下来是用户名和密码，这两个值以 \0 分割
步骤：
1.客户端把一个 Java 对象按照通信协议转换成二进制数据包
2.把这段二进制数据包发送到服务端，数据的传输过程由 TCP/IP 协议负责数据的传输，与我们的应用层无关
3.服务端接受到数据之后，按照协议取出二进制数据包中的相应字段，包装成 Java 对象，交给应用逻辑处理
4.服务端处理完之后，如果需要吐出响应给客户端，那么按照相同的流程进行