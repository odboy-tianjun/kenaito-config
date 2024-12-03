package cn.odboy.config.netty;

import cn.odboy.config.model.ConfigKv;
import cn.odboy.config.util.ProtostuffUtil;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
public class ConfigClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("当Channel已经注册到它的EventLoop并且能够处理I/O时被调用");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("当Channel从它的EventLoop注销并且无法处理任何I/O时被调用");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("当Channel处于活动状态（已经连接到它的远程节点）时被调用");
        System.err.println("MyClientHandler发送数据");
        //ctx.writeAndFlush("测试String编解码");
        // 测试对象编解码
        //ctx.writeAndFlush(new ConfigKv("app.config", "张三"));
        // 测试用protostuff对对象编解码
        ByteBuf buf = Unpooled.copiedBuffer(ProtostuffUtil.serializer(new ConfigKv("app.config", "张三")));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("当Channel离开活动状态并且不再连接到它的远程节点时被调用");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("收到服务器消息:" + msg);
    }
}
