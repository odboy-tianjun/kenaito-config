package cn.odboy.rest;

import cn.odboy.config.context.ClientPropertyHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class DemoController {
  @Value("${kenaito.config-center.test}")
  private String testStr;
  private final ConfigCenterProperties configCenterProperties;
  private final ClientPropertyHelper clientPropertyHelper;

  /** 配置变化了 */
  @GetMapping("/test")
  public ResponseEntity<Object> test() {
    String propertyName = "kenaito.config-center.test";
    System.err.println("@Value注解的值1=" + testStr);
    System.err.println("@ConfigurationProperties注解的值1=" + configCenterProperties.getTest());
    clientPropertyHelper.updateValue(propertyName, "Hello World");
    System.err.println("@Value注解的值2=" + testStr);
    System.err.println("@ConfigurationProperties注解的值2=" + configCenterProperties.getTest());
    return ResponseEntity.ok("success");
  }
}
