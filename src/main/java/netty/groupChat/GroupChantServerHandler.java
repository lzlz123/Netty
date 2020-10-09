package netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;


public class GroupChantServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel 组 ，管理所有的channel  填入的是全局的事件执行器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //当连接建立时候，一旦连接第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将该客户加入聊天的信息，推送给其他在线的客户端
        channelGroup.writeAndFlush("客户端：" + ctx.channel().remoteAddress() + "  加入聊天  " + "   " + sdf.format(new java.util.Date()));
        channelGroup.add(ctx.channel());
    }

    //断开连接,将XX客户离开推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客户端：" + channel.remoteAddress() + "  离开聊天  ");
        System.out.println("group的大小：" + channelGroup.size());
    }

    //表示：channel处于活动状态，提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        //分别处理
        channelGroup.forEach(t -> {
            if (channel != t) {
                t.writeAndFlush("客户：" + channel.remoteAddress() + "发送消息：" + s);
            } else {
                t.writeAndFlush("自己发送了消息:" + s);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
