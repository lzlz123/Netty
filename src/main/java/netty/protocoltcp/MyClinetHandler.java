package netty.protocoltcp;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class MyClinetHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private static int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {


        System.out.println("客户端接受到消息：   " + messageProtocol.toString() + "第   " + (count++) + "次");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //使用客户端发送十条数据
        for (int i = 0; i < 10; i++) {
            String s = "今天天气冷，吃火锅";
            byte[] bytes = s.getBytes(Charset.forName("utf-8"));
            MessageProtocol messageProtocol = new MessageProtocol(bytes.length, bytes);
            ctx.writeAndFlush(messageProtocol);
        }
    }


}
