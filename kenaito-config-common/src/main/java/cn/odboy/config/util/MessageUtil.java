package cn.odboy.config.util;

import cn.odboy.config.model.SmallMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MessageUtil {
    public static ByteBuf toByteBuf(Object data){
        return Unpooled.copiedBuffer(ProtostuffUtil.serializer(data));
    }

    public static SmallMessage getMessage(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return ProtostuffUtil.deserializer(bytes, SmallMessage.class);
    }
}
