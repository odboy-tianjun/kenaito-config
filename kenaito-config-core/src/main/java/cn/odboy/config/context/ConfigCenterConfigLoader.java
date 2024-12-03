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
import org.springframework.core.env.MutablePropertySources;

/**
 * 配置加载器
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
    @Autowired
    private ConfigCenterProperties properties;
    @Bean
    public BeanFactoryPostProcessor configLoader(ConfigurableEnvironment environment) {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                String cacheDir;
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith(OS_TYPE_WIN)) {
                    cacheDir = DEFAULT_PATH_WIN;
                } else if (os.toLowerCase().startsWith(OS_TYPE_MAC)) {
                    cacheDir = DEFAULT_PATH_MAC;
                }else {
                    cacheDir = DEFAULT_PATH_MAC;
                }
                environment.getProperty("kenaito.config-center.server", String.class, DEFAULT_CONFIG_SERVER);
                environment.getProperty("kenaito.config-center.port", Integer.class, DEFAULT_CONFIG_PORT);
                environment.getProperty("kenaito.config-center.env", String.class, DEFAULT_CONFIG_ENV);
                environment.getProperty("kenaito.config-center.data-id", String.class, DEFAULT_CONFIG_DATA_ID);
                environment.getProperty("kenaito.config-center.cache-dir", String.class, cacheDir);
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
