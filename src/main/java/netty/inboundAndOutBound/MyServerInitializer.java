package netty.inboundAndOutBound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.inboundAndOutBound.client.MyLongToByteEncoder;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MyLongToByteEncoder());

        //入栈的handler 进行解码===>入
        pipeline.addLast(new MyByteToLongDecoder());
        //出

        //入
        pipeline.addLast(new MyServerHandler());
        System.out.println("xxx");
    }
}
