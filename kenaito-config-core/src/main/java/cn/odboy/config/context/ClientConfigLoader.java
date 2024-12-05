package cn.odboy.config.context;

import cn.hutool.core.io.FileUtil;
import cn.odboy.config.model.msgtype.ClientInfo;
import cn.odboy.config.netty.ConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置加载器
 *
 * @author odboy
 * @date 2024-12-03
 */
@Slf4j
@Configuration
public class ClientConfigLoader {
    private static final String OS_TYPE_WIN = "win";
    private static final String OS_TYPE_MAC = "mac";
    private static final String DEFAULT_PATH_WIN = "c:\\data";
    private static final String DEFAULT_PATH_MAC = "/home/admin/data";
    private static final String DEFAULT_CONFIG_SERVER = "127.0.0.1";
    private static final Integer DEFAULT_CONFIG_PORT = 28002;
    private static final String DEFAULT_CONFIG_ENV = "default";
    private static final String DEFAULT_CONFIG_DATA_ID = "default";
    private static final String DEFAULT_PATH_WIN_SEP = ":";
    /**
     * 默认配置项
     */
    private static final String DEFAULT_CONFIG_NAME_SERVER = "kenaito.config-center.server";
    private static final String DEFAULT_CONFIG_NAME_PORT = "kenaito.config-center.port";
    private static final String DEFAULT_CONFIG_NAME_ENV = "kenaito.config-center.env";
    private static final String DEFAULT_CONFIG_NAME_DATA_ID = "kenaito.config-center.data-id";
    private static final String DEFAULT_CONFIG_NAME_CACHE_DIR = "kenaito.config-center.cache-dir";
    /**
     * 路径分割符
     */
    private static final String DEFAULT_PATH_SEP_WIN = "\\";
    private static final String DEFAULT_PATH_SEP_MAC = "/";
    /**
     * 当前客户端配置
     */
    public static final ClientInfo clientInfo = new ClientInfo();
    /**
     * 配置是否加载完毕
     */
    public static boolean isConfigLoaded = false;
    /**
     * 所有的配置信息
     */
    public static Map<String, Object> lastConfigs = new HashMap<>();

    @Bean
    public BeanFactoryPostProcessor configLoader(ConfigurableEnvironment environment) {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                String defaultCacheDir = getDefaultCacheDir();
                clientInfo.setServer(environment.getProperty(DEFAULT_CONFIG_NAME_SERVER, String.class, DEFAULT_CONFIG_SERVER));
                clientInfo.setPort(environment.getProperty(DEFAULT_CONFIG_NAME_PORT, Integer.class, DEFAULT_CONFIG_PORT));
                clientInfo.setEnv(environment.getProperty(DEFAULT_CONFIG_NAME_ENV, String.class, DEFAULT_CONFIG_ENV));
                clientInfo.setDataId(environment.getProperty(DEFAULT_CONFIG_NAME_DATA_ID, String.class, DEFAULT_CONFIG_DATA_ID));
                clientInfo.setCacheDir(environment.getProperty(DEFAULT_CONFIG_NAME_CACHE_DIR, String.class, defaultCacheDir));
                log.info("客户端属性: {}", clientInfo);
                validateCacheDirPath(defaultCacheDir, clientInfo.getCacheDir());
                createCacheDir(clientInfo.getCacheDir());
                try {
                    ConfigClient client = new ConfigClient();
                    client.start(clientInfo.getServer(), clientInfo.getPort());
                    // 这里加个同步锁，等客户端准备就绪后，拉取配置完成时
                    synchronized (clientInfo) {
                        while (!isConfigLoaded) {
                            try {
                                // 等待配置加载完成
                                clientInfo.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                log.error("Thread interrupted", e);
                            }
                        }
                        MapPropertySource propertySource = new MapPropertySource("loadFormServer", lastConfigs);
                        environment.getPropertySources().addFirst(propertySource);
                    }
                } catch (InterruptedException e) {
                    log.error("Netty Client Start Error", e);
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static String getDefaultCacheDir() {
        String defaultCacheDir;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(OS_TYPE_WIN)) {
            defaultCacheDir = DEFAULT_PATH_WIN;
        } else if (os.toLowerCase().startsWith(OS_TYPE_MAC)) {
            defaultCacheDir = DEFAULT_PATH_MAC;
        } else {
            defaultCacheDir = DEFAULT_PATH_MAC;
        }
        return defaultCacheDir;
    }

    private static void validateCacheDirPath(String defaultCacheDir, String cacheDir) {
        if (defaultCacheDir.contains(DEFAULT_PATH_WIN_SEP) && !cacheDir.contains(DEFAULT_PATH_WIN_SEP)) {
            throw new RuntimeException(DEFAULT_CONFIG_NAME_CACHE_DIR + " 配置的路径不正确");
        }
        if (cacheDir.contains(DEFAULT_PATH_WIN_SEP) && !cacheDir.contains(DEFAULT_PATH_SEP_WIN)) {
            throw new RuntimeException(DEFAULT_CONFIG_NAME_CACHE_DIR + " 配置的路径不正确, 正确的路径示范, " + DEFAULT_PATH_WIN);
        }
    }

    /**
     * 创建缓存文件夹
     */
    private static void createCacheDir(String cacheDir) {
        Path path = Paths.get(cacheDir);
        if (!Files.exists(path)) {
            File mkdir = FileUtil.mkdir(cacheDir);
            if (!mkdir.canWrite()) {
                throw new RuntimeException("缓存文件夹创建失败, 无读写权限");
            }
        }
    }
}
