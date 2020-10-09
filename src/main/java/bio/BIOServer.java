package bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //使用线程池机制
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        //监听端口
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动");
        while (true) {
            //监听，等待客户端连接，这里会阻塞
            Socket socket = serverSocket.accept();
            System.out.println("客户端连接成功");
            newCachedThreadPool.execute(new Runnable() {
                //重写方法
                public void run() {

                }
            });
        }
    }
    //编写一个handler方法和客户端通信
    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            //获取与输入流
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
                int read = inputStream.read(bytes);
                //当read = -1时候，就是读取结束
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
