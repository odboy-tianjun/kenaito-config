package cn.odboy.config.util;

/**
 * 配置命名空间 名称
 *
 * @author odboy
 * @date 2024-12-06
 */
public class PropertyNameUtil {
  private static final String DEFAULT_PREFIX = "kenaito";

  /**
   * 根据文件名生成带有默认前缀的文件名
   *
   * @param fileName 文件名，不包含路径信息
   * @return 带有默认前缀的文件名，格式为：DEFAULT_PREFIX_fileName
   */
  public static String get(String fileName) {
    return DEFAULT_PREFIX + "_" + fileName;
  }

}
