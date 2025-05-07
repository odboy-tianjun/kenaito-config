package cn.odboy.rest;

import cn.odboy.domain.ConfigApp;
import cn.odboy.domain.ConfigAppEnv;
import cn.odboy.infra.netty.ConfigClientManage;
import cn.odboy.infra.response.PageArgs;
import cn.odboy.service.ConfigAppEnvService;
import cn.odboy.service.ConfigAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置环境 前端控制器
 *
 * @author odboy
 * @since 2024-12-09
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/config/env")
public class ConfigEnvController {
    private final ConfigAppEnvService configAppEnvService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Validated @RequestBody ConfigAppEnv args) {
        configAppEnvService.create(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/queryList")
    public ResponseEntity<Object> queryList(@Validated @RequestBody ConfigAppEnv args) {
        return new ResponseEntity<>(configAppEnvService.queryList(args), HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@Validated @RequestBody ConfigAppEnv args) {
        configAppEnvService.remove(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
