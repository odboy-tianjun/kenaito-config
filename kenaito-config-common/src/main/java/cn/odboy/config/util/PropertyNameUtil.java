package cn.odboy.config.util;

/**
 * 配置命名空间 名称
 *
 * @author odboy
 * @date 2024-12-06
 */
public class PropertyNameUtil {
  private static final String DEFAULT_PREFIX = "kenaito";

  public static String get(String fileName) {
    return DEFAULT_PREFIX + "_" + fileName;
  }
}
