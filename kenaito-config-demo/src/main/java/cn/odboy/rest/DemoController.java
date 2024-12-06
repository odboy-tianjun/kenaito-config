package cn.odboy.rest;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局更新配置测试
 *
 * @author odboy
 * @date 2024-12-06
 */
@RestController
@RequestMapping("/updateConfig")
public class DemoController {
  @Value("${kenaito.config-center.demo:123}")
  private String demoStr;

  @Autowired private ConfigurableEnvironment environment;

  /** 配置变化了 */
  @GetMapping("/test")
  public ResponseEntity<Object> test() {
    String key = "kenaito.config-center.demo";
    String property = environment.getProperty(key);
    System.err.println("property=" + property);
    Map<String, Object> properties = new HashMap<>();
    properties.put(key, "Hello World!");
    MapPropertySource propertySource = new MapPropertySource("dynamicProperties", properties);
    // 将新的属性源添加到 ConfigurableEnvironment 中
    environment.getPropertySources().addFirst(propertySource);
    String property1 = environment.getProperty(key);
    System.err.println("property1=" + property1);
    return ResponseEntity.ok("success");
  }
}
