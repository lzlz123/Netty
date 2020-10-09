package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/*
说明：
1.我们 自定义的handler 需要继承 netty 的某个规定好的 HandlerAdapter
* */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取事件，获取客户端发送的消息
    /*
    ChannelHandlerContext ctx :上下文对象，包含 管道，通道， 地址
    Object msg :  客户端发送的数据 ，默认是OBJ的形式

    * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("服务器读取线程：" + Thread.currentThread().getName());
//        System.out.println("server ctx=" + ctx);
//        System.out.println("看看管道和channel的关心");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表
//
//        //将 msg 转为 byteBuf
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端的地址是：" + ctx.channel().remoteAddress());

        //如果出现一个耗时间非常长的任务，需要执行异步任务，
//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端,汪汪汪1", CharsetUtil.UTF_8));
//        System.out.println("go on  ....");
        //解决方案一：用户自定义普通任务,这样子可以提交到 NIOEventLoop的taskQueue中执行
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端,汪汪汪1", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("go on  ....");

        //解决方案二： 用户自定义定时任务 ==》提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端,汪汪汪1", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    //读取数据完毕，回送消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //数据写到缓冲区，并且打到缓存区
        //一般来讲，需要编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端,汪汪汪2", CharsetUtil.UTF_8));
    }

    //处理异常
    //一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
