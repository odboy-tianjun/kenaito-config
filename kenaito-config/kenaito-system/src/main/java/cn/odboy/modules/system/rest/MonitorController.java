package cn.odboy.modules.system.rest;

import cn.odboy.modules.system.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 系统-服务监控管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitor")
public class MonitorController {

    private final MonitorService serverService;

    /**
     * 查询服务监控
     */
    @GetMapping
    @PreAuthorize("@el.check('monitor:list')")
    public ResponseEntity<Object> queryMonitor() {
        return new ResponseEntity<>(serverService.getServers(), HttpStatus.OK);
    }
}
