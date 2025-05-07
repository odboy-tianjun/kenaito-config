package cn.odboy.infra.monitor;

import cn.odboy.infra.rest.AnonymousGetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统：健康检查接口
 * @author odboy
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthCheckController {
    @AnonymousGetMapping(value = "/check")
    public ResponseEntity<?> doCheck() {
        return ResponseEntity.ok().build();
    }
}
