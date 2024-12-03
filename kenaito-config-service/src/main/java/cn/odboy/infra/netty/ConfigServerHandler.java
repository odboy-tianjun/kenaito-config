package cn.odboy.infra.netty;

import cn.odboy.config.model.ConfigKv;
import cn.odboy.config.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConfigServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("当从Channel读取数据时被调用");
        //System.out.println("从客户端读取到String：" + msg.toString());
        //System.out.println("从客户端读取到Object：" + ((User)msg).toString());
        //测试用protostuff对对象编解码
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.err.println("从客户端读取到Object：" + ProtostuffUtil.deserializer(bytes, ConfigKv.class));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
