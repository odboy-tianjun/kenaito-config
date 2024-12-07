package cn.odboy.config.context;

import cn.hutool.core.util.StrUtil;
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
 * @author odboy
 * @date 2024-12-07
 */
@Component
@RequiredArgsConstructor
public class ClientPropertyHelper {
  private final ConfigurableEnvironment environment;
  private final ValueAnnotationProcessor valueAnnotationProcessor;

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
      if (propertySources.contains(ClientConfigLoader.PROPERTY_SOURCE_NAME)) {
        // 更新属性值
        PropertySource<?> propertySource =
            propertySources.get(ClientConfigLoader.PROPERTY_SOURCE_NAME);
        Map<String, Object> source = ((MapPropertySource) propertySource).getSource();
        source.put(propertyName, value);
      }
      // 单独更新@Value对应的值
      valueAnnotationProcessor.setValue(propertyName, value);
    }
  }
}
