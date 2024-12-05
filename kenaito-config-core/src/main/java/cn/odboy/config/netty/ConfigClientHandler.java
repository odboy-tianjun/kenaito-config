package cn.odboy.config.netty;

import cn.odboy.config.constant.ConfigClientMsgType;
import cn.odboy.config.context.ClientConfigLoader;
import cn.odboy.config.model.SmallMessage;
import cn.odboy.config.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
        SmallMessage smallMessage = new SmallMessage();
        smallMessage.setType(ConfigClientMsgType.REGISTER);
        smallMessage.setResp(SmallMessage.Response.ok(ClientConfigLoader.clientProp));
        ByteBuf buf = Unpooled.copiedBuffer(ProtostuffUtil.serializer(smallMessage));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ConfigClientHandler -> 当Channel离开活动状态并且不再连接到它的远程节点时被调用");
//        this.configClient.reConnect();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("ConfigClientHandler -> 当从Channel读取数据时被调用, 收到服务器消息");
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.err.println("ConfigClientHandler -> 从服务端读取到Object：" + ProtostuffUtil.deserializer(bytes, SmallMessage.class));
        SmallMessage smallMessage = ProtostuffUtil.deserializer(bytes, SmallMessage.class);
        if (smallMessage.getType() == ConfigClientMsgType.REGISTER) {

        }
    }
}
