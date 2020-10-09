package netty.dubbo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {


    private ChannelHandlerContext context;
    private String result;
    private String para;

    /**
     * 与服务器创建连接后就会被掉哟
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(2);
        //其他方法会使用到
        context = ctx;
    }

    /**
     * 收到服务器的数据后调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        System.out.println(4);
        //唤醒等待的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器，等待被唤醒，发送之后，就变成wait
    @Override
    public synchronized Object call() throws Exception {
        System.out.println(3);
        context.writeAndFlush(para);
        //进行wait
        wait();//等待 channread 返回结果唤醒我
        return result;
    }

    void setPara(String para) {
        this.para = para;
    }
}
