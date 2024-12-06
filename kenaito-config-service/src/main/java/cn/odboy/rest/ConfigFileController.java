package cn.odboy.rest;

import cn.odboy.domain.ConfigApp;
import cn.odboy.domain.ConfigFile;
import cn.odboy.service.ConfigFileService;
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
@RequestMapping("/api/config/file")
public class ConfigFileController {
  private final ConfigFileService configFileService;

  @PostMapping("/create")
  public ResponseEntity<Object> create(@Validated @RequestBody ConfigFile.CreateArgs args) throws Exception {
    configFileService.create(args);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/remove")
  public ResponseEntity<Object> remove(@Validated @RequestBody ConfigFile.RemoveArgs args) {
    configFileService.remove(args);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/modifyFileContent")
  public ResponseEntity<Object> modifyFileContent(@Validated @RequestBody ConfigFile.ModifyFileContentArgs args) {
    configFileService.modifyFileContent(args);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
