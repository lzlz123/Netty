package netty.protocoltcp;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private static int i = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int len = messageProtocol.getLen();
        String string1 = new String(messageProtocol.getContent(), Charset.forName("utf-8"));
        System.out.println("服务器端接受到数据是：   " + string1 + "   第 " + (i++) + " 次接受");
        //回送数据
        String s = UUID.randomUUID().toString();
        byte[] bytes = s.getBytes("utf-8");
        MessageProtocol messageProtocol1 = new MessageProtocol(bytes.length, bytes);
        channelHandlerContext.writeAndFlush(messageProtocol1);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
