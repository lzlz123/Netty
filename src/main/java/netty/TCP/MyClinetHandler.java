package netty.TCP;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class MyClinetHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuffer) throws Exception {
        byte[] bytes = new byte[byteBuffer.readableBytes()];
        byteBuffer.readBytes(bytes);
        String string = new String(bytes, Charset.forName("utf-8"));
        System.out.println("客户端接受到消息：   " + string + "第   " + (count++) + "次");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送十条数据
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,Server" + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }
    }


}
