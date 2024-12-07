package cn.odboy.config.context;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.context.refresh.ConfigDataContextRefresher;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 客户端配置 辅助类
 * <p>
 * 依赖 spring-cloud-context
 *
 * @author odboy
 * @date 2024-12-07
 */
@Component
@RequiredArgsConstructor
public class ClientPropertyHelper {
    private final ConfigurableEnvironment environment;
    private final ValueAnnotationProcessor valueAnnotationProcessor;
//    private final ConfigDataContextRefresher configDataContextRefresher;
    private final ConfigPropertyContextRefresher contextRefresher;

    /**
     * 动态更新配置值
     *
     * @param propertyName 属性路径名
     * @param value        属性值
     */
    public void updateValue(String propertyName, Object value) {
        if (StrUtil.isNotBlank(propertyName)) {
            // 设置属性值
            MutablePropertySources propertySources = environment.getPropertySources();
            if (propertySources.contains(ClientConfigLoader.PROPERTY_SOURCE_NAME)) {
                // 更新属性值
                PropertySource<?> propertySource = propertySources.get(ClientConfigLoader.PROPERTY_SOURCE_NAME);
                Map<String, Object> source = ((MapPropertySource) propertySource).getSource();
                source.put(propertyName, value);
            }
            // 单独更新@Value对应的值
            valueAnnotationProcessor.setValue(propertyName, value);
            // 刷新上下文(解决 @ConfigurationProperties注解的类属性值更新 问题)
            // Spring Cloud只会对被@RefreshScope和@ConfigurationProperties标注的bean进行刷新
            // 这个方法主要做了两件事：刷新配置源，也就是PropertySource，然后刷新了@ConfigurationProperties注解的类
//            configDataContextRefresher.refresh();
            contextRefresher.refreshAll();
        }
    }
}
