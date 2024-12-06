package cn.odboy.rest;

import cn.odboy.base.model.SelectOption;
import cn.odboy.domain.ConfigApp;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.netty.ConfigClientManage;
import cn.odboy.infra.response.PageArgs;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.rest.AnonymousAccess;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.domain.vo.UserQueryCriteria;
import cn.odboy.service.ConfigAppService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

  @AnonymousAccess
  @GetMapping("/queryClientList")
  public ResponseEntity<Object> queryClientList() {
    return new ResponseEntity<>(ConfigClientManage.queryClientInfos("daily", "kenaito-config-demo"), HttpStatus.OK);
  }
}
