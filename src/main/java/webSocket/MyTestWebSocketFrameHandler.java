package webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class MyTestWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息" + msg.text());
        //回复消息
        channelGroup.writeAndFlush(new TextWebSocketFrame("服务器事件   " + LocalDateTime.now() + "   " + msg.text()));
    }

    //当web客户端连接后，触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
        System.out.println("handlerAdded 被调用:  " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用:  " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用:  " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生：  " + cause.getMessage());
        ctx.close();

    }
}
