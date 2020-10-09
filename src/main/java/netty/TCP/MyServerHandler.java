package netty.TCP;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static int i = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] buffer = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(buffer);
        String string = new String(buffer, Charset.forName("utf-8"));
        System.out.println("服务器端接受到数据是：   " + string + "   第 " + (i++) + " 次接受");
        //回送数据
        ByteBuf respongse = Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("utf-8"));
        channelHandlerContext.writeAndFlush(respongse);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
