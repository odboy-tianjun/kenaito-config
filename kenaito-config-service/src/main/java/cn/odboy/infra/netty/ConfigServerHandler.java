package cn.odboy.infra.netty;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.constant.ConfigClientMsgType;
import cn.odboy.config.model.SmallMessage;
import cn.odboy.config.model.msgtype.ClientProp;
import cn.odboy.config.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConfigServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("ServerHandler -> 当从Channel读取数据时被调用");
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.err.println("ServerHandler -> 从客户端读取到Object：" + ProtostuffUtil.deserializer(bytes, SmallMessage.class));
        SmallMessage smallMessage = ProtostuffUtil.deserializer(bytes, SmallMessage.class);
        if (ConfigClientMsgType.REGISTER == smallMessage.getType()) {
            SmallMessage.Response resp = smallMessage.getResp();
            if (!resp.getSuccess() || resp.getData() == null) {
                ctx.channel().writeAndFlush(new SmallMessage(ConfigClientMsgType.REGISTER, SmallMessage.Response.bad("解析客户端属性失败")));
                return;
            }
            ClientProp clientProp = (ClientProp) resp.getData();
            if (StrUtil.isBlank(clientProp.getEnv())) {
                ctx.channel().writeAndFlush(new SmallMessage(ConfigClientMsgType.REGISTER, SmallMessage.Response.bad("解析客户端属性失败")));
                return;
            }
            if (StrUtil.isBlank(clientProp.getDataId())) {
                ctx.channel().writeAndFlush(new SmallMessage(ConfigClientMsgType.REGISTER, SmallMessage.Response.bad("解析客户端属性失败")));
                return;
            }
            ctx.channel().writeAndFlush(new SmallMessage(ConfigClientMsgType.REGISTER, SmallMessage.Response.ok(null)));
            ConfigClientManage.register(clientProp.getEnv(), clientProp.getDataId(), ctx);
        } else if (ConfigClientMsgType.PULL_CONFIG == smallMessage.getType()) {

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("ServerHandler -> 当Channel发生异常被调用, " + ExceptionUtil.stacktraceToString(cause));
//        cause.printStackTrace();
        ConfigClientManage.unregister(ctx.channel().id());
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ServerHandler -> 当Channel处于非活动状态（已经连接到它的远程节点）时被调用");
        ConfigClientManage.unregister(ctx.channel().id());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ServerHandler -> 当Channel从它的EventLoop注销并且不能够处理I/O时被调用");
        ConfigClientManage.unregister(ctx.channel().id());
    }
}
