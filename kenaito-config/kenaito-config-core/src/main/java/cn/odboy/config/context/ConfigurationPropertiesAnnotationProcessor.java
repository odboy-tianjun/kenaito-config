package cn.odboy.config.context;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载并处理@ConfigurationProperties对应的引用
 *
 * @author odboy
 * @date 2024-12-07
 */
@Component
public class ConfigurationPropertiesAnnotationProcessor implements BeanPostProcessor {
    private final Logger logger =
            LoggerFactory.getLogger(ConfigurationPropertiesAnnotationProcessor.class);
    @Getter
    private final Map<String, Object> prefixBeanMap = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        while (clazz != null) {
            if (clazz.isAnnotationPresent(ConfigurationProperties.class)) {
                // 排除springboot框架的配置
                if (beanName.contains("springframework")) {
                    clazz = clazz.getSuperclass();
                    continue;
                }
                // 排除数据源框架的配置
                if (beanName.contains("dataSource") || beanName.contains("druid")) {
                    clazz = clazz.getSuperclass();
                    continue;
                }
                // 排除ORM框架的配置
                if (beanName.contains("mybatis")) {
                    clazz = clazz.getSuperclass();
                    continue;
                }
                // 排除ip2region的配置
                if (beanName.contains("ip2region")) {
                    clazz = clazz.getSuperclass();
                    continue;
                }
                logger.info("扫描到自定义的@ConfigurationProperties注解类: {}", beanName);
                this.processConfigBean(clazz, bean);
            }
            clazz = clazz.getSuperclass();
        }
        return bean;
    }

    private void processConfigBean(Class<?> clazz, Object bean) {
        ConfigurationProperties annotation = clazz.getAnnotation(ConfigurationProperties.class);
        // 比如: kenaito.config-center
        prefixBeanMap.put(annotation.prefix(), bean);
    }
}
