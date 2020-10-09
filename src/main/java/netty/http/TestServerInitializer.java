package netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //向管道加入处理器
        //1.得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //加入netty提供的  httpServerCodec ===> 用于编解码
        //加入TestHttpServerHandler  ==》自己定义的
        pipeline.addLast("my HttpServerCodec", new HttpServerCodec()).
                addLast("我的 TestHttpServerHandler", new TestHttpServerHandler());
    }
}
