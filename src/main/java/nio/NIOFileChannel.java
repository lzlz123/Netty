package nio;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel {
    public static void main(String[] args) throws IOException {
        //想要放入的支付出
        String str = new String("哈哈哈");
        //创建一个流对象
        FileOutputStream fileOutputStream = new FileOutputStream("01.text");
        //根据流对象创建一个channel
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //将str放入buffer中
        buffer.put(str.getBytes());
        //将buffer转变为读取
        buffer.flip();
        //将buffer中的数据写入channel
        fileChannel.write(buffer);
        //关闭流
        fileOutputStream.close();


    }
}
