package cn.odboy.config.util;

import cn.odboy.config.model.msgtype.ConfigFileInfo;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化工具类，基于protobuf封装
 */
public class ProtostuffUtil {

  private static final Map<Class<?>, Schema<?>> CACHED_SCHEMA =
      new ConcurrentHashMap<Class<?>, Schema<?>>();

  /**
   * 获取指定类的Schema Schema用于描述对象的结构，以便于序列化和反序列化 该方法首先尝试从缓存中获取Schema，如果缓存中没有，则创建一个新的Schema并添加到缓存中
   *
   * @param clazz 需要获取Schema的类
   * @return 指定类的Schema
   */
  private static <T> Schema<T> getSchema(Class<T> clazz) {
    @SuppressWarnings("unchecked")
    Schema<T> schema = (Schema<T>) CACHED_SCHEMA.get(clazz);
    if (schema == null) {
      schema = RuntimeSchema.getSchema(clazz);
      if (schema != null) {
        CACHED_SCHEMA.put(clazz, schema);
      }
    }
    return schema;
  }

  /**
   * 序列化对象 使用Protostuff库将对象序列化为字节数组
   *
   * @param obj 需要序列化的对象
   * @return 序列化后的字节数组
   */
  public static <T> byte[] serializer(T obj) {
    @SuppressWarnings("unchecked")
    Class<T> clazz = (Class<T>) obj.getClass();
    LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    try {
      Schema<T> schema = getSchema(clazz);
      return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    } finally {
      buffer.clear();
    }
  }

  /**
   * 反序列化字节数组为对象 使用Protostuff库将字节数组反序列化为指定类的对象
   *
   * @param data 序列化后的字节数组
   * @param clazz 需要反序列化的对象类
   * @return 反序列化后的对象
   */
  public static <T> T deserializer(byte[] data, Class<T> clazz) {
    try {
      T obj = clazz.newInstance();
      Schema<T> schema = getSchema(clazz);
      ProtostuffIOUtil.mergeFrom(data, obj, schema);
      return obj;
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  public static void main(String[] args) {
    byte[] userBytes = ProtostuffUtil.serializer(new ConfigFileInfo());
    ConfigFileInfo user = ProtostuffUtil.deserializer(userBytes, ConfigFileInfo.class);
    System.out.println(user);
  }
}
