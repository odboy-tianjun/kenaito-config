package cn.odboy.rest;

import cn.odboy.config.context.ValueAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DemoController {
  @Value("${kenaito.config-center.demo:123}")
  private String demoStr;

  @Autowired private ValueAnnotationBeanPostProcessor valueAnnotationBeanPostProcessor;

  /** 配置变化了 */
  @GetMapping("/test")
  public ResponseEntity<Object> test() {
    System.err.println("demoStr=" + demoStr);
    String propertyName = "kenaito.config-center.demo";
    valueAnnotationBeanPostProcessor.setValue(propertyName, "xxxxxxxxxx");
    System.err.println("demoStr=" + demoStr);
    return ResponseEntity.ok("success");
  }
}
