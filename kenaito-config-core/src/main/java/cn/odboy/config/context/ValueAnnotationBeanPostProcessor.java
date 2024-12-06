package cn.odboy.config.context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ValueAnnotationBeanPostProcessor implements BeanPostProcessor {
  private final Logger logger = LoggerFactory.getLogger(ValueAnnotationBeanPostProcessor.class);
  private final Map<String, Field> nameFieldMap = new HashMap<>();
  private final Map<String, Object> nameBeanMap = new HashMap<>();

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> clazz = bean.getClass();
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Value.class)) {
          field.setAccessible(true);
          // 对@Value的处理
          Value annotation = field.getAnnotation(Value.class);
          String propertyName = annotation.value();
          String substring =
              propertyName.substring(propertyName.indexOf("${") + 2, propertyName.lastIndexOf("}"));
          // 处理带有默认值的配置
          propertyName = substring;
          if (substring.contains(":")) {
            String[] splits = substring.split(":");
            if (splits.length == 2) {
              propertyName = splits[0];
            }
          }
          nameFieldMap.put(propertyName, field);
          nameBeanMap.put(propertyName, bean);
        }
      }
      clazz = clazz.getSuperclass();
    }
    return bean;
  }

  public void setValue(String propertyName, Object value) {
    try {
      Field field = nameFieldMap.getOrDefault(propertyName, null);
      if (field != null) {
        field.setAccessible(true);
        field.set(nameBeanMap.get(propertyName), value);
      }
    } catch (IllegalAccessException e) {
      logger.error("配置 {} 字段值 {} 失败", propertyName, value, e);
    }
  }

  public Map<String, Field> getNameFieldMap() {
    return nameFieldMap;
  }
}
