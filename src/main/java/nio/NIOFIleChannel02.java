package nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFIleChannel02 {
    public static void main(String[] args) throws IOException {
        //创建一个流对象
        FileInputStream fileInputStream = new FileInputStream("01.text");
        //根据流对象创建一个channel
        FileChannel fileChannel = fileInputStream.getChannel();
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //将通道数据读取到buffer
        fileChannel.read(buffer);
        //将buffer转变为读取
        buffer.flip();
        System.out.println(new String(buffer.array()));


        //关闭流
        fileInputStream.close();

    }
}
