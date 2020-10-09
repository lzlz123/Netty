package netty.inboundAndOutBound.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.inboundAndOutBound.MyByteToLongDecoder;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap handler = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new MyLongToByteEncoder());
                    pipeline.addLast(new MyByteToLongDecoder());
                    pipeline.addLast(new MyClientHandler());
                }
            });
            ChannelFuture localhost = handler.connect("localhost", 7000).sync();
            localhost.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
