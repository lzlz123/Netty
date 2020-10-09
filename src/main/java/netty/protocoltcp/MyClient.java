package netty.protocoltcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap handler = new Bootstrap().group(group).channel(NioSocketChannel.class).
                    handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //编码处理
                            pipeline.addLast(new MyMessageDecoder());
                            pipeline.addLast(new MyMessgeEncoder());
                            pipeline.addLast(new MyClinetHandler());

                        }
                    });
            ChannelFuture localhost = handler.connect("localhost", 7000).sync();
            localhost.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
