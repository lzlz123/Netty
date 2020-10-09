package webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup boosGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //因为是基于http协议的，需要使用http的编码器解码
                            pipeline.addLast(new HttpServerCodec());
                            //是用方式写的
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                             * 说明
                             * 1.因为HTTP传输过程中是分段的，HttpObjectAggregator 可以将多个段聚合起来
                             * 2.这就是为什么 浏览器发送大量数据时候，就会发送多次http请求
                             * */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                             * 1.对于 websocke 其数据是用 帧 的形式传递的
                             * 2.可以看到  webSocketFrame 下面有六个子类
                             * 3. 浏览器请求时候 ws://localhost :7000/xxx 表示请求的uri
                             * 4.WebSocketServerProtocolHandler核心功能是将 http协议升级为 ws协议
                             * */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //处理业务逻辑的处理器
                            pipeline.addLast(new MyTestWebSocketFrameHandler());
                        }
                    });
            System.out.println("服务器启动");
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
