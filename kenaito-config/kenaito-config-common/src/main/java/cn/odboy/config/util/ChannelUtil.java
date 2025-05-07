package cn.odboy.config.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

/**
 * 统一操作Channel
 *
 * @author odboy
 * @date 2024-12-06
 */
public class ChannelUtil {
  /**
   * 根据ChannelHandlerContext获取通道的唯一标识符 该方法用于在处理通道相关的操作时，能够快速获取到通道的唯一标识符，以便进行后续的处理
   *
   * @param ctx 通道的上下文对象，包含了通道的所有相关信息和操作方法
   * @return 返回通道的唯一标识符，以短文本形式呈现
   */
  public static String getId(ChannelHandlerContext ctx) {
    return ctx.channel().id().asShortText();
  }

  /**
   * 根据ChannelId获取通道的唯一标识符 当只有通道的标识符时，可以使用该方法获取通道的唯一标识符的短文本形式
   *
   * @param channelId 通道的标识符对象，唯一标识了一个通道
   * @return 返回通道的唯一标识符，以短文本形式呈现
   */
  public static String getId(ChannelId channelId) {
    return channelId.asShortText();
  }
}
