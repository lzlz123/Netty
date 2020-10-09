package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        //创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("01.text");
        //创建输入流的channel
        FileChannel channel = fileInputStream.getChannel();
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(1);
        //创建一个输出流对象
        FileOutputStream fileOutputStream = new FileOutputStream("02.text");
        //创建输出流对象的channel
        FileChannel channel1 = fileOutputStream.getChannel();

        //循环读取
        while (true) {
            buffer.clear();//需要重置一下buffer
            int read = channel.read(buffer);
            System.out.println(read);
            //表示读取结束
            if (read == -1) {
                break;
            }
            buffer.flip();
            channel1.write(buffer);
        }
        //从buffer中写出数据
        channel1.write(buffer);
        //关闭
        fileInputStream.close();
        fileOutputStream.close();


    }
}
