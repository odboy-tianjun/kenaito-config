package cn.odboy.config.context;

import cn.hutool.core.util.StrUtil;
import cn.odboy.config.constant.ClientConfigConsts;
import cn.odboy.config.constant.ClientConfigVars;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 客户端配置 辅助类
 *
 * <p>依赖 spring-cloud-context
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
   * @param value 属性值
   */
  public void updateValue(String propertyName, Object value) {
    if (StrUtil.isNotBlank(propertyName)) {
      // 设置属性值
      MutablePropertySources propertySources = environment.getPropertySources();
      if (propertySources.contains(ClientConfigConsts.PROPERTY_SOURCE_NAME)) {
        // 更新属性值
        PropertySource<?> propertySource =
            propertySources.get(ClientConfigConsts.PROPERTY_SOURCE_NAME);
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

  /**
   * 更新所有配置属性<br>
   * 此方法遍历缓存的配置，更新应用程序中的相应属性 <br>
   * 它主要针对的是那些使用@Value注解注入的配置属性 <br>
   * 当缓存的配置发生变化时，通过此方法可以确保应用中的配置是最新的
   */
  public void updateAll() {
    // 获取所有可变属性源
    MutablePropertySources propertySources = environment.getPropertySources();
    // 检查是否包含特定的属性源
    if (propertySources.contains(ClientConfigConsts.PROPERTY_SOURCE_NAME)) {
      // 获取属性源
      PropertySource<?> propertySource =
          propertySources.get(ClientConfigConsts.PROPERTY_SOURCE_NAME);
      // 将属性源转换为Map形式，以便于更新属性
      Map<String, Object> source = ((MapPropertySource) propertySource).getSource();
      // 遍历缓存的配置
      for (Map.Entry<String, Object> kvMap : ClientConfigVars.cacheConfigs.entrySet()) {
        // 更新属性值
        source.put(kvMap.getKey(), kvMap.getValue());
        // 单独更新@Value对应的值
        valueAnnotationProcessor.setValue(kvMap.getKey(), kvMap.getValue());
      }
      // 刷新所有应用上下文，使更新后的配置生效
      contextRefresher.refreshAll();
    }
  }
}
