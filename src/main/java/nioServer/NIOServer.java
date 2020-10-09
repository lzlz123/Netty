package nioServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建 SercerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到selector的对象
        Selector selector = Selector.open();

        //绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到select，关心的时间是  op_accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环监听
        while (true) {
            //等待一秒
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了一秒，无连接");
                continue;
            }
            //如果返回的不是0
            //获取到，事件的集合  ==》存在事件发生的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历 一个 set
            Iterator<SelectionKey> keyiterator = selectionKeys.iterator();
            while (keyiterator.hasNext()) {
                SelectionKey key = keyiterator.next();
                //分别处理事件,根据key发生的事件，做响应的处理、
                //如果发生的是连接事件
                if (key.isAcceptable()) {
                    //给该客户端生成socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个 socketChannel " + socketChannel.hashCode());
                    //设定socketChannel设定为非阻塞
                    socketChannel.configureBlocking(false);
                    //关注的是读事件，同时给该socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //如果发生的是读事件
                if (key.isReadable()) {
                    //通过 key 方向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel 关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from客户端" + new String(buffer.array()));
                }
                //移除当前的 key 防止重复操作
                keyiterator.remove();
            }

        }
    }
}
