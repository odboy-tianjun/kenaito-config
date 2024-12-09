package cn.odboy.config.constant;

import cn.odboy.config.model.msgtype.ClientInfo;

/**
 * 客户端常量
 *
 * @author odboy
 * @date 2024-12-09
 */
public class ClientConfigConsts {
    /**
     * 默认的配置值
     */
    public static final String OS_TYPE_WIN = "win";

    public static final String OS_TYPE_MAC = "mac";
    public static final String DEFAULT_PATH_WIN = "c:\\data";
    public static final String DEFAULT_PATH_MAC = "/home/admin/data";
    public static final String DEFAULT_CONFIG_SERVER = "127.0.0.1";
    public static final Integer DEFAULT_CONFIG_PORT = 28010;
    public static final String DEFAULT_CONFIG_ENV = "default";
    public static final String DEFAULT_CONFIG_DATA_ID = "default";
    public static final String DEFAULT_PATH_WIN_SEP = ":";

    /**
     * 默认配置项：配置中心服务ip
     */
    public static final String DEFAULT_CONFIG_NAME_SERVER = "kenaito.config-center.server";

    /**
     * 默认配置项：配置中心服务端口
     */
    public static final String DEFAULT_CONFIG_NAME_PORT = "kenaito.config-center.port";

    /**
     * 默认配置项：将拉取的配置环境
     */
    public static final String DEFAULT_CONFIG_NAME_ENV = "kenaito.config-center.env";

    /**
     * 默认配置项：将拉取配置的应用的名称
     */
    public static final String DEFAULT_CONFIG_NAME_DATA_ID = "kenaito.config-center.data-id";

    /**
     * 默认配置项：配置缓存目录
     */
    public static final String DEFAULT_CONFIG_NAME_CACHE_DIR = "kenaito.config-center.cache-dir";

    /**
     * Win路径分割符
     */
    public static final String DEFAULT_PATH_SEP_WIN = "\\";

    /**
     * Mac路径分割符
     */
    public static final String DEFAULT_PATH_SEP_MAC = "/";

    /**
     * 配置源名称
     */
    public static final String PROPERTY_SOURCE_NAME = "kenaito-dynamic-config";
    /**
     * 当前客户端配置
     */
    public static final ClientInfo clientInfo = new ClientInfo();
}
