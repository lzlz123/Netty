package netty.dubbo.provider;

import netty.dubbo.netty.NettyServer;

//启动服务的提供者
public class ServerBootStrap {
    public static void main(String[] args) {
        NettyServer.StartServer("127.0.0.1", 7000);

    }
}
