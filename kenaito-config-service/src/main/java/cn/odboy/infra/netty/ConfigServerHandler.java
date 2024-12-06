package cn.odboy.infra.netty;

import cn.hutool.core.util.StrUtil;
import cn.odboy.config.model.SmallMessage;
import cn.odboy.config.model.msgtype.ClientInfo;
import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.config.util.ChannelUtil;
import cn.odboy.config.util.MessageUtil;
import cn.odboy.service.ConfigFileService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端命令处理
 *
 * @author odboy
 * @date 2024-12-06
 */
@Slf4j
public class ConfigServerHandler extends ChannelInboundHandlerAdapter {
  private final ConfigFileService configFileService;

  public ConfigServerHandler(ConfigFileService configFileService) {
    this.configFileService = configFileService;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    /// log.info("ServerHandler -> 当从Channel读取数据时被调用");
    SmallMessage smallMessage = MessageUtil.getMessage(msg);
    log.info("ServerHandler -> 从客户端读取到Object：{}", smallMessage);
    SmallMessage.Response resp = smallMessage.getResp();
    switch (smallMessage.getType()) {
      case REGISTER:
        if (!resp.getSuccess() || resp.getData() == null) {
          ctx.writeAndFlush(MessageUtil.toRegisterBad("解析客户端属性失败"));
          break;
        }
        ClientInfo clientInfo = (ClientInfo) resp.getData();
        if (StrUtil.isBlank(clientInfo.getEnv())) {
          ctx.writeAndFlush(MessageUtil.toRegisterBad("解析客户端属性失败"));
          break;
        }
        if (StrUtil.isBlank(clientInfo.getDataId())) {
          ctx.writeAndFlush(MessageUtil.toRegisterBad("解析客户端属性失败"));
          break;
        }
        ConfigClientManage.register(clientInfo.getEnv(), clientInfo.getDataId(), ctx);
        ctx.writeAndFlush(MessageUtil.toRegisterOk(null));
        break;
      case PULL_CONFIG:
        if (!resp.getSuccess()) {
          log.info("ServerHandler -> 客户端说它没有准备好拉取配置");
          break;
        }
        log.info("ServerHandler -> 客户端说它准备好拉取配置了");
        String[] envDataId = ConfigClientManage.getEnvDataId(ctx.channel().id());
        String env = envDataId[0];
        String dataId = envDataId[1];
        List<Channel> channels = ConfigClientManage.queryChannels(env, dataId);
        if (channels.isEmpty()) {
          log.info("ServerHandler -> 没有在线的客户端");
          break;
        }
        List<ConfigFileInfo> fileList = configFileService.getFileList(env, dataId);
        if (fileList.isEmpty()) {
          // 广播
          for (Channel channel : channels) {
            try {
              channel.writeAndFlush(
                  MessageUtil.toPushConfigBad(String.format("应用 %s 没有环境编码为 %s 的配置", dataId, env)));
            } catch (Exception e) {
              log.error(
                  "ServerHandler -> 推送无法获取配置的消息到客户端失败, env: {}, dataId={}, channelId={}",
                  env,
                  dataId,
                  ChannelUtil.getId(channel.id()),
                  e);
            }
          }
        } else {
          // 广播
          for (Channel channel : channels) {
            try {
              ctx.writeAndFlush(MessageUtil.toPushConfigOk(fileList));
              log.info(
                  "ServerHandler -> 推送配置到客户端成功, env: {}, dataId={}, channelId={}",
                  env,
                  dataId,
                  ChannelUtil.getId(channel.id()));
            } catch (Exception e) {
              log.error(
                  "ServerHandler -> 推送配置到客户端失败, env: {}, dataId={}, channelId={}",
                  env,
                  dataId,
                  ChannelUtil.getId(channel.id()),
                  e);
            }
          }
        }
        break;
      default:
        break;
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("ServerHandler -> 当Channel发生异常被调用", cause);
    /// cause.printStackTrace();
    ConfigClientManage.unregister(ctx.channel().id());
    ctx.close();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("ServerHandler -> 当Channel处于非活动状态（已经连接到它的远程节点）时被调用");
    ConfigClientManage.unregister(ctx.channel().id());
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    log.info("ServerHandler -> 当Channel从它的EventLoop注销并且不能够处理I/O时被调用");
    ConfigClientManage.unregister(ctx.channel().id());
  }
}
