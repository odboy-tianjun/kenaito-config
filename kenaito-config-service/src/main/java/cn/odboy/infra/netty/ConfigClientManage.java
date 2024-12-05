package cn.odboy.infra.netty;

import cn.odboy.infra.exception.BadRequestException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 客户端管理
 *
 * @author odboy
 * @date 2024-12-04
 */
public class ConfigClientManage {
    /**
     * 所有的客户端连接: {env}_{dataId} to ctx
     */
    private static final ConcurrentMap<String, ChannelHandlerContext> CLIENT = new ConcurrentHashMap<>();
    /**
     * 所有的客户端Id: channelId to {env}_{dataId}
     */
    private static final ConcurrentMap<ChannelId, String> CHANNEL = new ConcurrentHashMap<>();

    public static void register(String env, String dataId, ChannelHandlerContext ctx) {
        String envClientKey = String.format("%s_%s", env, dataId);
        CLIENT.put(envClientKey, ctx);
        CHANNEL.put(ctx.channel().id(), envClientKey);
        System.err.println("ConfigClientManage -> 客户端注册成功");
        System.err.println("ConfigClientManage -> ctx.channel.id=" + ctx.channel().id());
    }

    public static void unregister(ChannelId channelId) {
        String envClientKey = CHANNEL.getOrDefault(channelId, null);
        if (envClientKey != null) {
            CHANNEL.remove(channelId);
            ChannelHandlerContext ctx = CLIENT.getOrDefault(envClientKey, null);
            if (ctx != null) {
                CLIENT.remove(envClientKey);
                System.err.println("ConfigClientManage -> 客户端注销成功");
                System.err.println("ConfigClientManage -> ctx.channel.id=" + channelId);
            }
        }
    }

    public static String[] getEnvDataId(ChannelId channelId) {
        String envDataId = CHANNEL.getOrDefault(channelId, null);
        if (envDataId == null) {
            throw new BadRequestException("获取配置数据ID失败");
        }
        String[] s = envDataId.split("_");
        if (s.length != 2) {
            throw new BadRequestException("获取配置数据ID失败");
        }
        return s;
    }
}
