package cn.odboy.config.util;

import cn.odboy.config.constant.TransferMessageType;
import cn.odboy.config.model.SmallMessage;
import cn.odboy.config.model.msgtype.ConfigFileInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;

/**
 * 传输消息序列化
 *
 * @author odboy
 * @date 2024-12-06
 */
public class MessageUtil {
  public static ByteBuf toByteBuf(Object data) {
    return Unpooled.copiedBuffer(ProtostuffUtil.serializer(data));
  }

  public static SmallMessage getMessage(Object msg) {
    ByteBuf buf = (ByteBuf) msg;
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);
    return ProtostuffUtil.deserializer(bytes, SmallMessage.class);
  }

  // ================ 以下为很糙的自定义方法
  public static ByteBuf toRegisterBad(String errorMessage) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.bad(errorMessage)));
  }

  public static ByteBuf toRegisterOk(Object data) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.ok(data)));
  }

  public static ByteBuf toPushConfigBad(String errorMessage) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.PUSH_CONFIG, SmallMessage.Response.bad(errorMessage)));
  }

  public static ByteBuf toPushConfigOk(Object data) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.PUSH_CONFIG, SmallMessage.Response.ok(data)));
  }

  public static List<ConfigFileInfo> toConfigFileInfoList(Object o) {
    if (o instanceof List) {
      List<?> list = (List<?>) o;
      if (!list.isEmpty() && list.get(0) instanceof ConfigFileInfo) {
        return (List<ConfigFileInfo>) list;
      }
    }
    return new ArrayList<>();
  }
}
