package cn.odboy.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import java.util.Map;
/**
 * 配置加载器
 * @author odboy
 * @date 2024-12-03
 */
@Configuration
public class ConfigCenterConfigLoader {
    @Bean
    public BeanFactoryPostProcessor configLoader(ConfigurableEnvironment environment) {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//                ConfigCenterClient client = new ConfigCenterClient("http://your-config-center-url/config");
//                Map<String, Object> configKv = client.fetchConfig();
//                MapPropertySource propertySource = new MapPropertySource("configCenter", configKv);
//                environment.getPropertySources().addFirst(propertySource);
            }
        };
    }
}
