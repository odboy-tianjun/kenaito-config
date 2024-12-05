package cn.odboy.infra.netty;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.constant.TransferMessageType;
import cn.odboy.config.model.SmallMessage;
import cn.odboy.config.model.msgtype.ClientInfo;
import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.config.util.MessageUtil;
import cn.odboy.service.ConfigFileService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class ConfigServerHandler extends ChannelInboundHandlerAdapter {
    private final ConfigFileService configFileService;

    public ConfigServerHandler(ConfigFileService configFileService) {
        this.configFileService = configFileService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.err.println("ServerHandler -> 当从Channel读取数据时被调用");
        SmallMessage smallMessage = MessageUtil.getMessage(msg);
        System.err.println("ServerHandler -> 从客户端读取到Object：" + smallMessage);
        SmallMessage.Response resp = smallMessage.getResp();
        switch (smallMessage.getType()) {
            case REGISTER:
                if (!resp.getSuccess() || resp.getData() == null) {
                    ctx.writeAndFlush(MessageUtil.toByteBuf(new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.bad("解析客户端属性失败"))));
                    return;
                }
                ClientInfo clientInfo = (ClientInfo) resp.getData();
                if (StrUtil.isBlank(clientInfo.getEnv())) {
                    ctx.writeAndFlush(MessageUtil.toByteBuf(new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.bad("解析客户端属性失败"))));
                    return;
                }
                if (StrUtil.isBlank(clientInfo.getDataId())) {
                    ctx.writeAndFlush(MessageUtil.toByteBuf(new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.bad("解析客户端属性失败"))));
                    return;
                }
                ConfigClientManage.register(clientInfo.getEnv(), clientInfo.getDataId(), ctx);
                ctx.writeAndFlush(MessageUtil.toByteBuf(new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.ok(null))));
                break;
            case PULL_CONFIG:
                if (!resp.getSuccess()) {
                    System.err.println("ServerHandler -> 客户端说它没有准备好拉取配置");
                    return;
                }
                System.err.println("ServerHandler -> 客户端说它准备好拉取配置了");
                String[] envDataId = ConfigClientManage.getEnvDataId(ctx.channel().id());
                String env = envDataId[0];
                String dataId = envDataId[1];
                List<ConfigFileInfo> fileList = configFileService.getFileList(env, dataId);
                if (fileList.isEmpty()) {
                    ctx.writeAndFlush(MessageUtil.toByteBuf(new SmallMessage(TransferMessageType.PUSH_CONFIG, SmallMessage.Response.bad(
                            String.format("应用 %s 没有环境编码为 %s 的配置", dataId, env)
                    ))));
                } else {
                    System.err.println("ServerHandler -> 推送配置到客户端");
                    ctx.writeAndFlush(MessageUtil.toByteBuf(new SmallMessage(TransferMessageType.PUSH_CONFIG, SmallMessage.Response.ok(fileList))));
                }
                break;
            default:
                break;
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
