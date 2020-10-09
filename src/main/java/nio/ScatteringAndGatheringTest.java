package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        //scattering:将数据写入buffer时候，可与采用buffer数组，依次写入
        //Gathering:从buffer中读取数据的时候，可与用buffer数据，依次读取
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建一个buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        ByteBuffer allocate = ByteBuffer.allocate(2);
        ByteBuffer allocate1 = ByteBuffer.allocate(6);
        byteBuffers[1] = allocate;
        byteBuffers[0] = allocate1;

        //等待客户端连接
        SocketChannel socket = serverSocketChannel.accept();
        //循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < 8) {
                long l = socket.read(byteBuffers);//只需要传入数组，会自动分配
                byteRead += l;
                System.out.println("累计读取的个数" + byteRead);
                //使用流打印，看看当前的buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "posistion" + buffer.position() + ",limit=" + buffer.limit()).forEach(System.out::println);

            }
            //将所有的buffer进行一次反转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
            //将数据显示到客户端
            long byteWrite = 0;
            while (byteWrite < 8) {
                long write = socket.write(byteBuffers);
                byteWrite += write;
            }
            //将所有的buffer进行一次复位
            Arrays.asList(byteBuffers).forEach(Buffer -> {
                Buffer.clear();
            });
            System.out.println(byteRead + "     " + byteWrite);
        }

    }
}
