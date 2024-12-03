package cn.odboy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kenaito.config-center")
public class ConfigCenterProperties {
    private String meta;
    private String dataId;
    private String cacheDir;
    private String env;
}
