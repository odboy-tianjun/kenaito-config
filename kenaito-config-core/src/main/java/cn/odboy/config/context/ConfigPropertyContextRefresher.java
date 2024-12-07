package cn.odboy.config.context;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置属性上下文 刷新
 *
 * @author odboy
 * @date 2024-12-07
 */
@Component
@RequiredArgsConstructor
public class ConfigPropertyContextRefresher {
    private final ConfigurationPropertiesAnnotationProcessor processor;
    private final ConfigurableEnvironment environment;

    /**
     * 刷新单个属性
     *
     * @param propertyName 属性名表达式
     * @param value        属性值
     */
    public void refreshSingle(String propertyName, Object value) {
        MutablePropertySources propertySources = environment.getPropertySources();
        if (propertySources.contains(ClientConfigLoader.PROPERTY_SOURCE_NAME)) {
            // 更新属性值
            PropertySource<?> propertySource = propertySources.get(ClientConfigLoader.PROPERTY_SOURCE_NAME);
            Map<String, Object> source = ((MapPropertySource) propertySource).getSource();
            source.put(propertyName, value);
        } else {
            // 新增属性值
            Map<String, Object> propertyMap = new HashMap<>(1);
            MapPropertySource propertySource = new MapPropertySource(ClientConfigLoader.PROPERTY_SOURCE_NAME, propertyMap);
            propertySources.addFirst(propertySource);
        }
        // 使用 Binder 重新绑定 @ConfigurationProperties
        Binder binder = Binder.get(environment);
        for (Map.Entry<String, Object> propertyPrefixBean : processor.getPrefixBeanMap().entrySet()) {
            binder.bind(propertyPrefixBean.getKey(), Bindable.ofInstance(propertyPrefixBean.getValue()));
        }
    }

    /**
     * 刷新所有属性
     */
    public void refreshAll() {
        MutablePropertySources propertySources = environment.getPropertySources();
        if (propertySources.contains(ClientConfigLoader.PROPERTY_SOURCE_NAME)) {
            // 替换属性值
            MapPropertySource propertySource = new MapPropertySource(ClientConfigLoader.PROPERTY_SOURCE_NAME, ClientConfigLoader.cacheConfigs);
            propertySources.replace(ClientConfigLoader.PROPERTY_SOURCE_NAME, propertySource);
        } else {
            // 新增属性值
            Map<String, Object> propertyMap = new HashMap<>(1);
            MapPropertySource propertySource = new MapPropertySource(ClientConfigLoader.PROPERTY_SOURCE_NAME, propertyMap);
            propertySources.addFirst(propertySource);
        }
        // 使用 Binder 重新绑定 @ConfigurationProperties
        Binder binder = Binder.get(environment);
        for (Map.Entry<String, Object> propertyPrefixBean : processor.getPrefixBeanMap().entrySet()) {
            binder.bind(propertyPrefixBean.getKey(), Bindable.ofInstance(propertyPrefixBean.getValue()));
        }
    }
}
