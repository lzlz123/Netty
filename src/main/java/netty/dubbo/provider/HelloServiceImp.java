package netty.dubbo.provider;

import netty.dubbo.publicInterface.HelloService;

public class HelloServiceImp implements HelloService {
    //当有人消费者调用该方法时候，返回结果
    @Override
    public String hello(String msg) {
        System.out.println("收到消费者传递的消息：   " + msg);
        if (msg != null) {
            return "你好，客户端，我已经收到你的消息，   " + msg;
        } else {
            return "你好，客户端，请你再说一遍";
        }

    }
}
