package cn.odboy.rest;

import cn.odboy.domain.ConfigApp;
import cn.odboy.infra.netty.ConfigClientManage;
import cn.odboy.infra.response.PageArgs;
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
 * 配置文件 前端控制器
 *
 * @author odboy
 * @since 2024-12-05
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/config/app")
public class ConfigAppController {
    private final ConfigAppService configAppService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Validated @RequestBody ConfigApp.CreateArgs args) {
        configAppService.create(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@Validated @RequestBody ConfigApp.RemoveArgs args) {
        configAppService.remove(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/modifyDescription")
    public ResponseEntity<Object> modifyDescription(@Validated @RequestBody ConfigApp.ModifyDescriptionArgs args) {
        configAppService.modifyDescription(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/queryPage")
    public ResponseEntity<Object> queryPage(@Validated @RequestBody PageArgs<ConfigApp> args) {
        return new ResponseEntity<>(configAppService.queryPage(args), HttpStatus.OK);
    }

    @PostMapping("/queryClientList")
    public ResponseEntity<Object> queryClientList(@Validated @RequestBody ConfigApp.QueryClientArgs args) {
        return new ResponseEntity<>(ConfigClientManage.queryClientInfos(args), HttpStatus.OK);
    }
}
