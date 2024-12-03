package cn.odboy.config.netty;

import cn.odboy.config.model.ConfigKv;
import cn.odboy.config.util.ProtostuffUtil;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
public class ConfigCenterClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到服务器消息:" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler发送数据");
        //ctx.writeAndFlush("测试String编解码");
        //测试对象编解码
        //ctx.writeAndFlush(new User(1,"zhuge"));
        //测试用protostuff对对象编解码
        ByteBuf buf = Unpooled.copiedBuffer(ProtostuffUtil.serializer(new ConfigKv("app.config", "张三")));
        ctx.writeAndFlush(buf);
    }
}
