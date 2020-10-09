package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //当读取事件时候，触发
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
        //判断 httpObject 是不是一个httprequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg 的类型:" + msg.getClass());
            System.out.println("客户端的地址”：" + channelHandlerContext.channel().remoteAddress());

            //回复信息给浏览器[http的协议]
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);
            //构造一个http 的对uing，httpresponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //将构建好的response防护
            channelHandlerContext.writeAndFlush(response);


        }
    }
}
