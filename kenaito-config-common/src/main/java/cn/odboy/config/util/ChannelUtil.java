package cn.odboy.config.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
/**
 * 统一操作Channel
 * @author odboy
 * @date 2024-12-06
 */
public class ChannelUtil {
    public static String getId(ChannelHandlerContext ctx){
        return ctx.channel().id().asShortText();
    }

    public static String getId(ChannelId channelId){
        return channelId.asShortText();
    }
}
