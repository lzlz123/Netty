package netty.inboundAndOutBound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("3，当前的handler是：   " + "MyServerHandler");
        System.out.println("4. 我的hashcode是：   " + this.hashCode());
        System.out.println("5. 从客户端:   " + channelHandlerContext.channel().remoteAddress() + "    读取的数据是:   " + aLong);
        System.out.println("6.给客户端回送");
    //    channelHandlerContext.writeAndFlush(1234L);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("7.测试专用channelReadComplete");
            ctx.writeAndFlush(Unpooled.copyLong(213123L));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
