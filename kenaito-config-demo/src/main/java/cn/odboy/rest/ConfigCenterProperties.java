package cn.odboy.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 注册中心配置
 *
 * @author odboy
 * @date 2024-12-07
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kenaito.config-center")
public class ConfigCenterProperties {
  private String server;
  private Integer port;
  private String dataId;
  private String env;
  private String cacheDir;
}
