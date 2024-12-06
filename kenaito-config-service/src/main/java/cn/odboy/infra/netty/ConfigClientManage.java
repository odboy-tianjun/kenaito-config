package cn.odboy.infra.netty;

import cn.odboy.config.util.ChannelUtil;
import cn.odboy.domain.ConfigApp;
import cn.odboy.infra.exception.BadRequestException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端管理
 *
 * @author odboy
 * @date 2024-12-04
 */
@Slf4j
public class ConfigClientManage {
  /** 所有的客户端连接: {env}_{dataId}_{channelId} to ctx */
  private static final ConcurrentMap<String, Channel> CLIENT = new ConcurrentHashMap<>();

  /**
   * 查询客户端节点列表
   *
   * @param env 环境编码
   * @param dataId 应用名称
   * @return /
   */
  public static List<ConfigApp.ClientInfo> queryClientInfos(String env, String dataId) {
    String filterKey = String.format("%s_%s_", env, dataId);
    return CLIENT.entrySet().stream()
        .filter(f -> f.getKey().startsWith(filterKey))
        .map(Map.Entry::getValue)
        .map(
            m -> {
              ConfigApp.ClientInfo clientInfo = new ConfigApp.ClientInfo();
              clientInfo.setIp(m.remoteAddress().toString().replaceAll("/",""));
              clientInfo.setIsActive(m.isActive());
              return clientInfo;
            })
        .collect(Collectors.toList());
  }

  /**
   * 客户端注册
   *
   * @param env 环境编码
   * @param dataId 应用名称
   * @param ctx 信道
   */
  public static void register(String env, String dataId, ChannelHandlerContext ctx) {
    String envClientKey = String.format("%s_%s_%s", env, dataId, ChannelUtil.getId(ctx));
    CLIENT.put(envClientKey, ctx.channel());
    log.info("ConfigClientManage -> 客户端 {} 注册成功", envClientKey);
  }

  /**
   * 客户端注销
   *
   * @param channelId /
   */
  public static void unregister(ChannelId channelId) {
    List<String> envClientKeys =
        CLIENT.keySet().stream()
            .filter(f -> f.endsWith(ChannelUtil.getId(channelId)))
            .collect(Collectors.toList());
    for (String envClientKey : envClientKeys) {
      Channel channel = CLIENT.getOrDefault(envClientKey, null);
      if (channel != null) {
        if (channel.isOpen()) {
          channel.closeFuture();
        }
        CLIENT.remove(envClientKey);
        log.info("ConfigClientManage -> 客户端 {} 注销成功", envClientKey);
      }
    }
  }

  /**
   * 根据env和dataId查询所有客户端节点
   *
   * @param env 环境编码
   * @param dataId 应用名称
   * @return /
   */
  public static List<Channel> queryChannels(String env, String dataId) {
    String filterKey = String.format("%s_%s_", env, dataId);
    return CLIENT.entrySet().stream()
        .filter(f -> f.getKey().startsWith(filterKey))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  /**
   * 根据channelId获取env和dataId
   *
   * @param channelId /
   * @return /
   */
  public static String[] getEnvDataId(ChannelId channelId) {
    String envClientKey =
        CLIENT.keySet().stream()
            .filter(f -> f.endsWith(ChannelUtil.getId(channelId)))
            .findFirst()
            .orElse(null);
    if (envClientKey == null) {
      throw new BadRequestException("获取配置数据ID失败");
    }
    String[] s = envClientKey.split("_");
    // 最大分割块数
    int maxSplitLength = 3;
    if (s.length != maxSplitLength) {
      throw new BadRequestException("获取配置数据ID失败");
    }
    return s;
  }
}
