package netty.dubbo.customer;

import netty.dubbo.netty.NettyClient;
import netty.dubbo.publicInterface.HelloService;

public class ClientBootstrap {
    public static final String proto = "lizhen!";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();
        //创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, proto);
        String s = service.hello("你好 dubbo");
        System.out.println("调用的结果res=  " + s);

    }
}



