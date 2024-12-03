package cn.odboy.config.context;

import cn.odboy.config.ConfigCenterProperties;
import cn.odboy.config.netty.ConfigCenterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 配置加载器
 *
 * @author odboy
 * @date 2024-12-03
 */
@Slf4j
@Configuration
public class ConfigCenterConfigLoader {
    private static final String OS_TYPE_WIN = "win";
    private static final String OS_TYPE_MAC = "mac";
    private static final String DEFAULT_PATH_WIN = "c:/data";
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
    @Autowired
    private ConfigCenterProperties properties;

    @Bean
    public BeanFactoryPostProcessor configLoader(ConfigurableEnvironment environment) {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                String defaultCacheDir;
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith(OS_TYPE_WIN)) {
                    defaultCacheDir = DEFAULT_PATH_WIN;
                } else if (os.toLowerCase().startsWith(OS_TYPE_MAC)) {
                    defaultCacheDir = DEFAULT_PATH_MAC;
                } else {
                    defaultCacheDir = DEFAULT_PATH_MAC;
                }
                String server = environment.getProperty(DEFAULT_CONFIG_NAME_SERVER, String.class, DEFAULT_CONFIG_SERVER);
                Integer port = environment.getProperty(DEFAULT_CONFIG_NAME_PORT, Integer.class, DEFAULT_CONFIG_PORT);
                String env = environment.getProperty(DEFAULT_CONFIG_NAME_ENV, String.class, DEFAULT_CONFIG_ENV);
                String dataId = environment.getProperty(DEFAULT_CONFIG_NAME_DATA_ID, String.class, DEFAULT_CONFIG_DATA_ID);
                String cacheDir = environment.getProperty(DEFAULT_CONFIG_NAME_CACHE_DIR, String.class, defaultCacheDir);
                if (defaultCacheDir.contains(DEFAULT_PATH_WIN_SEP) && !cacheDir.contains(DEFAULT_PATH_WIN_SEP)) {
                    throw new RuntimeException(DEFAULT_CONFIG_NAME_CACHE_DIR + " 配置的路径不正确");
                }
                ConfigCenterClient client = new ConfigCenterClient();
                try {
                    client.start(properties);
                } catch (InterruptedException e) {
                    log.error("Netty Client Start Error", e);
                    throw new RuntimeException(e);
                }
//                ConfigCenterClient client = new ConfigCenterClient("http://your-config-center-url/config");
//                Map<String, Object> configKv = client.fetchConfig();
//                MapPropertySource propertySource = new MapPropertySource("configCenter", configKv);
//                environment.getPropertySources().addFirst(propertySource);
            }
        };
    }
}
