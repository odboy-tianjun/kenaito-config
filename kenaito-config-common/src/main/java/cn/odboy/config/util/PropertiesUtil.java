package cn.odboy.config.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * 工具类出处，https://blog.csdn.net/qq_27574367/article/details/134684434 <br>
 * fix: 函数flattenMap中value为null导致的异常中断 <br>
 *
 * @author Deng.Weiping
 * @since 2023/11/28 13:57
 */
@Slf4j
public class PropertiesUtil {
  private static final Pattern PATTERN = Pattern.compile("\\s*([^=\\s]*)\\s*=\\s*(.*)\\s*");
  private static final String PATH_SEP = ".";

  /**
   * 将 YAML 字符串转换为 Properties 字符串
   *
   * @param input YAML 字符串
   * @return Properties 字符串
   */
  public static String castToProperties(String input) {
    Map<String, Object> propertiesMap = new LinkedHashMap<>();
    Map<String, Object> yamlMap = new Yaml().load(input);
    flattenMap("", yamlMap, propertiesMap);
    return propertiesMap.entrySet().stream()
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .collect(Collectors.joining(StrUtil.LF));
  }

  /**
   * 将 Properties 字符串转换为 YAML 字符串
   *
   * @param input Properties 字符串
   * @return YAML 字符串
   */
  public static String castToYaml(String input) {
    try {
      Map<String, Object> properties = readProperties(input);
      return properties2Yaml(properties);
    } catch (Exception e) {
      log.error("property 转 Yaml 转换异常", e);
    }
    return null;
  }

  /**
   * 将 InputStream 中的 Properties 转换为 YAML 字符串
   *
   * @param inputStream 输入流
   * @return YAML 字符串
   */
  public static String castToYaml(InputStream inputStream) {
    try {
      Map<String, Object> properties =
          readProperties(StrUtil.str(inputStream.readAllBytes(), StandardCharsets.UTF_8));
      return properties2Yaml(properties);
    } catch (Exception e) {
      log.error("property 转 Yaml 转换异常", e);
    }
    return null;
  }

  /**
   * 将字节数组中的 Properties 转换为 YAML 字符串
   *
   * @param bytes 字节数组
   * @return YAML 字符串
   */
  public static String castToYaml(byte[] bytes) {
    try {
      Map<String, Object> properties = readProperties(StrUtil.str(bytes, StandardCharsets.UTF_8));
      return properties2Yaml(properties);
    } catch (Exception e) {
      log.error("property 转 Yaml 转换异常", e);
    }
    return null;
  }

  /**
   * 读取 Properties 字符串并转换为 Map
   *
   * @param input Properties 字符串
   * @return Map 对象
   */
  private static Map<String, Object> readProperties(String input) {
    Map<String, Object> propertiesMap = new LinkedHashMap<>();
    for (String line : input.split(StrUtil.LF)) {
      if (StrUtil.isNotBlank(line)) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.matches()) {
          String key = matcher.group(1);
          String value = matcher.group(2);
          propertiesMap.put(key, value);
        }
      }
    }
    return propertiesMap;
  }

  /**
   * 递归地将 Map 转换为 Properties 格式的 Map
   *
   * @param prefix 前缀
   * @param yamlMap YAML 格式的 Map
   * @param treeMap 目标 Properties 格式的 Map
   */
  private static void flattenMap(
      String prefix, Map<String, Object> yamlMap, Map<String, Object> treeMap) {
    yamlMap.forEach(
        (key, value) -> {
          if (value != null) {
            String fullKey = prefix + key;
            if (value instanceof LinkedHashMap) {
              flattenMap(fullKey + ".", (LinkedHashMap) value, treeMap);
            } else if (value instanceof ArrayList) {
              List<?> values = (List<?>) value;
              for (int i = 0; i < values.size(); i++) {
                String itemKey = String.format("%s[%d]", fullKey, i);
                Object itemValue = values.get(i);
                if (itemValue instanceof String) {
                  treeMap.put(itemKey, itemValue);
                } else {
                  flattenMap(itemKey + ".", (LinkedHashMap) itemValue, treeMap);
                }
              }
            } else {
              treeMap.put(fullKey, value.toString());
            }
          }
        });
  }

  /**
   * 将 Properties 格式的 Map 转换为 YAML 格式的字符串
   *
   * @param properties Properties 格式的 Map
   * @return YAML 格式的字符串
   */
  private static String properties2Yaml(Map<String, Object> properties) {
    if (CollUtil.isEmpty(properties)) {
      return null;
    }
    Map<String, Object> map = parseToMap(properties);
    return map2Yaml(map).toString();
  }

  /**
   * 递归地将 Properties 格式的 Map 解析为 LinkedHashMap
   *
   * @param propMap Properties 格式的 Map
   * @return LinkedHashMap 对象
   */
  private static Map<String, Object> parseToMap(Map<String, Object> propMap) {
    Map<String, Object> resultMap = new LinkedHashMap<>();
    if (CollectionUtils.isEmpty(propMap)) {
      return resultMap;
    }
    propMap.forEach(
        (key, value) -> {
          if (key.contains(PATH_SEP)) {
            String currentKey = key.substring(0, key.indexOf("."));
            if (resultMap.get(currentKey) != null) {
              return;
            }
            Map<String, Object> childMap = getChildMap(propMap, currentKey);
            Map<String, Object> map = parseToMap(childMap);
            resultMap.put(currentKey, map);
          } else {
            resultMap.put(key, value);
          }
        });
    return resultMap;
  }

  /**
   * 获取拥有相同父级节点的子节点
   *
   * @param propMap Properties 格式的 Map
   * @param currentKey 当前父级节点的键
   * @return 子节点的 Map
   */
  private static Map<String, Object> getChildMap(Map<String, Object> propMap, String currentKey) {
    Map<String, Object> childMap = new LinkedHashMap<>();
    propMap.forEach(
        (key, value) -> {
          if (key.contains(currentKey + PATH_SEP)) {
            String subKey = key.substring(key.indexOf(".") + 1);
            childMap.put(subKey, value);
          }
        });
    return childMap;
  }

  /**
   * 将 Map 集合转换为 YAML 格式的字符串
   *
   * @param map Map 对象
   * @return YAML 格式的字符串
   */
  public static StringBuffer map2Yaml(Map<String, Object> map) {
    return map2Yaml(map, 0);
  }

  /**
   * 将 Map 集合转换为 YAML 格式的字符串
   *
   * @param propMap Map 对象
   * @param deep 树的层级
   * @return YAML 格式的字符串
   */
  private static StringBuffer map2Yaml(Map<String, Object> propMap, int deep) {
    StringBuffer yamlBuffer = new StringBuffer();
    if (CollectionUtils.isEmpty(propMap)) {
      return yamlBuffer;
    }
    String space = getSpace(deep);
    for (Map.Entry<String, Object> entry : propMap.entrySet()) {
      Object valObj = entry.getValue();
      String key = space + entry.getKey() + ":";
      if (valObj instanceof String) {
        yamlBuffer.append(key).append(" ").append(valObj).append("\n");
      } else if (valObj instanceof List) {
        yamlBuffer.append(key).append("\n");
        List<String> list =
            ((List<String>) valObj).stream().map(Object::toString).collect(Collectors.toList());
        String lSpace = getSpace(deep + 1);
        for (String str : list) {
          yamlBuffer.append(lSpace).append("- ").append(str).append("\n");
        }
      } else if (valObj instanceof Map) {
        yamlBuffer.append(key).append("\n");
        yamlBuffer.append(map2Yaml((LinkedHashMap) valObj, deep + 1));
      } else {
        yamlBuffer.append(key).append(" ").append(valObj).append("\n");
      }
    }
    return yamlBuffer;
  }

  /**
   * 获取缩进空格
   *
   * @param deep 树的层级
   * @return 缩进空格字符串
   */
  private static String getSpace(int deep) {
    return "  ".repeat(Math.max(0, deep));
  }
}
