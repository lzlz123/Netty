package groupChat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    protected ServerSocketChannel listenChannel;
    private static final int port = 6667;

    //构造
    public GroupChatServer() throws IOException {
        selector = Selector.open();
        listenChannel = ServerSocketChannel.open();
        //绑定端口
        listenChannel.socket().bind(new InetSocketAddress(port));
        //阻塞模式
        listenChannel.configureBlocking(false);
        //注册
        listenChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    //监听代码
    public void listen() throws IOException {
        while (true) {
            int count = selector.select(2000);
            //有事件需要处理
            if (count > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel accept = listenChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                        System.out.println(accept.getRemoteAddress() + "上线");
                    }
                    if (key.isReadable()) {//通道发生read事件，通道可读
                        //处理读
                        read(key);
                    }
                    //删除
                    iterator.remove();
                }
            } else {
                System.out.println("等待中");
            }
        }
    }

    private void read(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            String string = null;
            //根据read的值处理
            if (read > 0) {
                string = new String(buffer.array());
                System.out.println("from客户端" + string);
            }
            //转发消息
            sendInfoToOtherClients(string, channel);
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //需要排除自己
    private void sendInfoToOtherClients(String msg, SocketChannel socketChannel) {
        Set<SelectionKey> keys = selector.keys();
        Iterator<SelectionKey> iterator = keys.iterator();
        System.out.println("服务器转发消息");
        try {
            while (iterator.hasNext()) {
                Channel channel = iterator.next().channel();
                if (channel instanceof SocketChannel && channel != socketChannel) {
                    SocketChannel channel1 = (SocketChannel) channel;
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    channel1.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
