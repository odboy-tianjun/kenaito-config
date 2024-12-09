package cn.odboy.config.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端变量
 *
 * @author odboy
 * @date 2024-12-09
 */
public class ClientConfigVars {

    /**
     * 配置是否加载完毕
     */
    public static boolean isConfigLoaded = false;

    /**
     * 服务器是否离线
     */
    public static boolean isServerOffline = false;

    /**
     * 原有的配置信息：filename -> file content
     */
    public static Map<String, String> originConfigs = new HashMap<>();

    /**
     * 转换后的配置信息：filename -> {configKey: configValue}
     */
    public static Map<String, Map<String, Object>> lastConfigs = new HashMap<>();

    /**
     * 所有自定义配置项缓存
     */
    public static Map<String, Object> cacheConfigs = new HashMap<>();
}
