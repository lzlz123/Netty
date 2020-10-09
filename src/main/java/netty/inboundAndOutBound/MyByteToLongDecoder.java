package netty.inboundAndOutBound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.Buffer;
import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * @param channelHandlerContext 上下文对象
     * @param byteBuf               入栈的byteBuf 对象
     * @param list                  list集合，传递给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("1. 当前的Handler是：  " + "MyByteToLongDecoder");
        System.out.println("2. 我的hashcode是：   " + this.hashCode());

        if (byteBuf.readableBytes() >= 8) {
            //必须大于八个直接才可以
            list.add(byteBuf.readLong());
        }
    }

}
