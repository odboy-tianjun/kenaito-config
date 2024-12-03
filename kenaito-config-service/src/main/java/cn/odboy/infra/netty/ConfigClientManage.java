package cn.odboy.infra.netty;

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
     * 所有的客户端连接: {env}-{dataId} to ctx
     */
    private static final ConcurrentMap<String, ChannelHandlerContext> clientMap = new ConcurrentHashMap<>();
    /**
     * 所有的客户端Id: channelId to {env}-{dataId}
     */
    private static final ConcurrentMap<ChannelId, String> channelMap = new ConcurrentHashMap<>();

    public static void register(String env, String dataId, ChannelHandlerContext ctx) {
        String envClientKey = String.format("%s_%s", env, dataId);
        clientMap.put(envClientKey, ctx);
        channelMap.put(ctx.channel().id(), envClientKey);
        System.err.println("ConfigClientManage -> 客户端注册成功");
        System.err.println("ConfigClientManage -> ctx.channel.id=" + ctx.channel().id());
    }

    public static void unregister(ChannelId channelId) {
        String envClientKey = channelMap.getOrDefault(channelId, null);
        if (envClientKey != null) {
            channelMap.remove(channelId);
            ChannelHandlerContext ctx = clientMap.getOrDefault(envClientKey, null);
            if (ctx != null) {
                clientMap.remove(envClientKey);
                System.err.println("ConfigClientManage -> 客户端注销成功");
                System.err.println("ConfigClientManage -> ctx.channel.id=" + channelId);
            }
        }
    }
}
