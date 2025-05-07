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
  /**
   * 将给定的对象序列化为ByteBuf实例 该方法使用Protostuff库对对象进行序列化，便于在网络传输或存储
   *
   * @param data 待序列化的对象
   * @return 序列化后的ByteBuf实例
   */
  public static ByteBuf toByteBuf(Object data) {
    return Unpooled.copiedBuffer(ProtostuffUtil.serializer(data));
  }

  /**
   * 反序列化ByteBuf为指定的SmallMessage对象 该方法主要用于处理接收到的字节数据，将其还原为对象形式
   *
   * @param msg 待反序列化的ByteBuf对象，被视为字节数据源
   * @return 反序列化后的SmallMessage对象
   */
  public static SmallMessage getMessage(Object msg) {
    ByteBuf buf = (ByteBuf) msg;
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);
    return ProtostuffUtil.deserializer(bytes, SmallMessage.class);
  }

  /**
   * 创建一个表示注册失败的ByteBuf消息 该方法用于生成一个包含错误信息的注册响应消息，便于在网络中传输
   *
   * @param errorMessage 注册失败的错误信息
   * @return 包含注册失败信息的ByteBuf消息
   */
  public static ByteBuf toRegisterBad(String errorMessage) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.bad(errorMessage)));
  }

  /**
   * 创建一个表示注册成功的ByteBuf消息 该方法用于生成一个包含成功信息的注册响应消息，便于在网络中传输
   *
   * @param data 注册成功时附带的数据
   * @return 包含注册成功信息的ByteBuf消息
   */
  public static ByteBuf toRegisterOk(Object data) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.REGISTER, SmallMessage.Response.ok(data)));
  }

  /**
   * 创建一个表示推送配置失败的ByteBuf消息 该方法用于生成一个包含错误信息的推送配置响应消息，便于在网络中传输
   *
   * @param errorMessage 推送配置失败的错误信息
   * @return 包含推送配置失败信息的ByteBuf消息
   */
  public static ByteBuf toPushConfigBad(String errorMessage) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.PUSH_CONFIG, SmallMessage.Response.bad(errorMessage)));
  }

  /**
   * 创建一个表示推送配置成功的ByteBuf消息 该方法用于生成一个包含成功信息的推送配置响应消息，便于在网络中传输
   *
   * @param data 推送配置成功时附带的数据
   * @return 包含推送配置成功信息的ByteBuf消息
   */
  public static ByteBuf toPushConfigOk(Object data) {
    return toByteBuf(
        new SmallMessage(TransferMessageType.PUSH_CONFIG, SmallMessage.Response.ok(data)));
  }

  /**
   * 将给定的对象转换为ConfigFileInfo对象列表 该方法主要用于处理接收到的消息，将其转换为配置文件信息列表 如果给定对象不是预期的列表类型或列表为空，则返回一个新的空列表
   *
   * @param o 待转换的对象
   * @return 转换后的ConfigFileInfo对象列表，如果转换失败则返回空列表
   */
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
