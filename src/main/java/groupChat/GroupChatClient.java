package groupChat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    protected SocketChannel socketChannel;
    private String username;

    //构造器
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到USername
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("客户端准备完成");
    }

    //向服务器发送消息
    public void sendINFO(String INFO) {
        String info = username + "说了：" + INFO;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readChannel = selector.select();
            if (readChannel > 0) {
                System.out.println("有可用通道");
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SocketChannel channel = (SocketChannel) iterator.next().channel();
                    ByteBuffer allocate = ByteBuffer.allocate(1024);
                    channel.read(allocate);
                    String msg = new String(allocate.array());
                    System.out.println("接收到的消息" + msg);
                }
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient charClient = new GroupChatClient();
        //启动一个线程
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    charClient.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            charClient.sendINFO(s);
        }
    }
}
