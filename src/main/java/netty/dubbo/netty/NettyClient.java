package netty.dubbo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    private static ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler clientHandler;

    //创建代理对象，使用代理模式
    public Object getBean(final Class<?> serviceClass, final String proto) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, (Proxy, method, args) -> {
            if (clientHandler == null) {
                System.out.println(1);
                initCilent();
            }
            //设置需要发送的信息,
            //协议头+数据
            clientHandler.setPara(proto + args[0]);
            return executors.submit(clientHandler).get();
        });
    }


    private static void initCilent() throws InterruptedException {
        clientHandler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture connect = new Bootstrap().group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringEncoder()).addLast(new StringDecoder());
                        pipeline.addLast(clientHandler);
                    }
                }).connect("127.0.0.1", 7000).sync();
    }

}

