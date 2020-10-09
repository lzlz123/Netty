package nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //buffer的使用
        //创建一个buffer
        //创建了一个大小为 5 的 int  缓冲区
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer中存放数据
        intBuffer.put(1);
        intBuffer.put(1);
        intBuffer.put(1);
        intBuffer.put(1);
        intBuffer.put(1);

        //读取数据==>读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

    }
}
