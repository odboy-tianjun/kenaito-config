package cn.odboy.config.context;

/**
 * 客户端属性
 *
 * @author odboy
 * @date 2024-12-03
 */
public class ClientProp {
    public static String server;
    public static Integer port;
    public static String env;
    public static String dataId;
    public static String cacheDir;

    public static String toPrint() {
        return String.format("server: %s, port: %s, env: %s, dataId: %s, cacheDir: %s",
                server,
                port,
                env,
                dataId,
                cacheDir
        );
    }
}
