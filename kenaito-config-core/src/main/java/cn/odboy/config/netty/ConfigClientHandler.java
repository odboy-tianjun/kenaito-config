package cn.odboy.config.netty;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.constant.TransferMessageType;
import cn.odboy.config.context.ClientConfigLoader;
import cn.odboy.config.model.SmallMessage;
import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.config.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * 配置中心客户端 业务处理
 *
 * @author odboy
 * @date 2024-12-06
 */
public class ConfigClientHandler extends ChannelInboundHandlerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ConfigClientHandler.class);

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    logger.info("当Channel已经注册到它的EventLoop并且能够处理I/O时被调用");
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    logger.info("当Channel从它的EventLoop注销并且无法处理任何I/O时被调用");
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    logger.info("当Channel处于活动状态（已经连接到它的远程节点）时被调用, 注册客户端");
    SmallMessage smallMessage = new SmallMessage();
    smallMessage.setType(TransferMessageType.REGISTER);
    smallMessage.setResp(SmallMessage.Response.ok(ClientConfigLoader.clientInfo));
    ctx.writeAndFlush(MessageUtil.toByteBuf(smallMessage));
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    logger.info("当Channel离开活动状态并且不再连接到它的远程节点时被调用");
    /// this.configClient.reConnect();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    /// logger.info("当从Channel读取数据时被调用, 收到服务器消息");
    logger.info("从服务端读取到Object");
    SmallMessage smallMessage = MessageUtil.getMessage(msg);
    SmallMessage.Response resp = smallMessage.getResp();
    switch (smallMessage.getType()) {
      case REGISTER:
        if (!resp.getSuccess()) {
          logger.info("注册失败, {}", resp.getErrorMessage());
        } else {
          logger.info("注册成功, 给服务端发信号, 表明准备好可以拉取配置了");
          // 准备拉取配置，给服务端发信号
          SmallMessage pullConfigMessage = new SmallMessage();
          pullConfigMessage.setType(TransferMessageType.PULL_CONFIG);
          pullConfigMessage.setResp(SmallMessage.Response.ok(null, "准备好拉取配置了"));
          ctx.writeAndFlush(MessageUtil.toByteBuf(pullConfigMessage));
        }
        break;
      case PUSH_CONFIG:
        if (!resp.getSuccess()) {
          throw new RuntimeException(resp.getErrorMessage());
        }
        List<ConfigFileInfo> configFileInfos = MessageUtil.toConfigFileInfoList(resp.getData());
        logger.info("收到来自服务端推送的配置信息");
        Map<String, String> originConfigs = new HashMap<>(1);
        Map<String, Map<String, Object>> lastConfigs = new HashMap<>(1);
        try {
          for (ConfigFileInfo configFileInfo : configFileInfos) {
            String fileName = configFileInfo.getFileName();
            // 配置原生内容
            originConfigs.put(fileName, configFileInfo.getFileContent());
            // 转换为应用能识别的配置项
            String suffix = FileUtil.getSuffix(fileName);
            boolean isYml = "yml".equals(suffix) || "yaml".equals(suffix);
            if (isYml) {
              Yaml yaml = new Yaml();
              lastConfigs.put(fileName, yaml.load(configFileInfo.getFileContent()));
            } else {
              Properties properties = new Properties();
              properties.load(StrUtil.getReader(configFileInfo.getFileContent()));
              Map<String, Object> tempMap = new HashMap<>(1);
              for (Map.Entry<Object, Object> kv : properties.entrySet()) {
                String key = (String) kv.getKey();
                tempMap.put(key, kv.getValue());
              }
              lastConfigs.put(fileName, tempMap);
            }
          }
          logger.info("配置文件转map成功");
          ClientConfigLoader.originConfigs = originConfigs;
          ClientConfigLoader.lastConfigs = lastConfigs;
          ClientConfigLoader.isConfigLoaded = true;
          synchronized (ClientConfigLoader.clientInfo) {
            // 通知所有等待的线程
            ClientConfigLoader.clientInfo.notifyAll();
          }
        } catch (IOException e) {
          logger.info("配置文件转map失败", e);
        }
        break;
      default:
        break;
    }
  }
}
