package netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {


        //创建BossGroup 和 workerGroup
        //说明：
        //1.创建两个线程组，分别是  bossGroup 和 workerGroup
        //2.boss 只初恋连接请求，  worker完成业务
        //3.都是无限循环
        //4.EventLoopGroup 和 EventLoopGroup 含有的子线程（NioEventLoop）的个数
        // 默认是 CPU的核数乘以2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            //创建服务器端的启动对象，配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //链式编程进行设置
            bootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//是nioSocketChannel 作为通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个 通道测试对象
                        //给 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务器 is ready...");
            //绑定一个端口，并且同步，生成了一个ChannelFuture 对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //给 cf注册监听器
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口成功");
                    } else {
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("服务端出错");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
