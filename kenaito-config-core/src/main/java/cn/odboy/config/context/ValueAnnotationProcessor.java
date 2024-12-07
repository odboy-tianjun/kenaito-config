package cn.odboy.config.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载并处理@Value对应的引用
 *
 * @author odboy
 * @date 2024-12-07
 */
@Component
public class ValueAnnotationProcessor implements BeanPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(ValueAnnotationProcessor.class);
    private final Map<String, Field> valueFieldMap = new HashMap<>();
    private final Map<String, Object> valueBeanMap = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        while (clazz != null) {
            // 处理@Value
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Value.class)) {
                    field.setAccessible(true);
                    // 对@Value的处理
                    Value annotation = field.getAnnotation(Value.class);
                    String propertyName = annotation.value();
                    propertyName =
                            propertyName.substring(propertyName.indexOf("${") + 2, propertyName.lastIndexOf("}"));
                    // 处理带有默认值的配置
                    if (propertyName.contains(":")) {
                        int firstSpIndex = propertyName.indexOf(":");
                        propertyName = propertyName.substring(0, firstSpIndex);
                    }
                    valueFieldMap.put(propertyName, field);
                    valueBeanMap.put(propertyName, bean);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return bean;
    }

    public void setValue(String propertyName, Object value) {
        try {
            Field field = valueFieldMap.getOrDefault(propertyName, null);
            if (field != null) {
                field.setAccessible(true);
                field.set(valueBeanMap.get(propertyName), value);
            }
        } catch (IllegalAccessException e) {
            logger.error("设置 {} 字段值 {} 失败", propertyName, value, e);
        }
    }
}
