package netty.dubbo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.dubbo.provider.HelloServiceImp;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("msg=  " + msg);
        //必须满足协议，才可以调用我的服务===>可以是某一个字符串
        if (msg.toString().startsWith("lizhen!")) {
            String hello = new HelloServiceImp().hello(msg.toString().substring(msg.toString().lastIndexOf("!") + 1));
            ctx.writeAndFlush(hello);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
