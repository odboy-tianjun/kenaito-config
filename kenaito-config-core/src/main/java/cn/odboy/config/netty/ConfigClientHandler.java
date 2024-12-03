package cn.odboy.config.netty;

import cn.odboy.config.constant.ConfigClientMsgType;
import cn.odboy.config.context.ClientConfigLoader;
import cn.odboy.config.model.Message;
import cn.odboy.config.util.ProtostuffUtil;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ConfigClientHandler extends ChannelInboundHandlerAdapter {
    private final ConfigClient configClient;
    public ConfigClientHandler(ConfigClient configClient) {
        this.configClient = configClient;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ConfigClientHandler -> 当Channel已经注册到它的EventLoop并且能够处理I/O时被调用");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ConfigClientHandler -> 当Channel从它的EventLoop注销并且无法处理任何I/O时被调用");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ConfigClientHandler -> 当Channel处于活动状态（已经连接到它的远程节点）时被调用, 注册客户端");
        Message message = new Message();
        message.setType(ConfigClientMsgType.REGISTER);
        message.setData(ClientConfigLoader.clientProp);
        ByteBuf buf = Unpooled.copiedBuffer(ProtostuffUtil.serializer(message));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ConfigClientHandler -> 当Channel离开活动状态并且不再连接到它的远程节点时被调用");
//        this.configClient.reConnect();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("ConfigClientHandler -> 收到服务器消息:" + msg);
    }
}
