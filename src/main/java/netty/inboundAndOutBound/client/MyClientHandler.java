package netty.inboundAndOutBound.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        ChannelPipeline pipeline = channelHandlerContext.pipeline();
        System.out.println("服务器的IP地址是：   " + channelHandlerContext.channel().remoteAddress());
        System.out.println("收到服务器的数据是：   " + aLong);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送数据");
        ctx.writeAndFlush(1233L);
    }
}
