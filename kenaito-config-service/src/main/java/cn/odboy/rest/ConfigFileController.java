package cn.odboy.rest;

import cn.odboy.domain.ConfigFile;
import cn.odboy.service.ConfigFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 配置环境 前端控制器
 *
 * @author odboy
 * @since 2024-12-09
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/config/file")
public class ConfigFileController {
    private final ConfigFileService configFileService;

    @PostMapping("/queryList")
    public ResponseEntity<Object> queryList(@RequestBody ConfigFile.QueryList args) {
        return new ResponseEntity<>(configFileService.queryList(args), HttpStatus.OK);
    }

    @PostMapping("/getContentById")
    public ResponseEntity<Object> getContentById(@RequestBody ConfigFile args) {
        return new ResponseEntity<>(configFileService.getContentById(args), HttpStatus.OK);
    }

    @PostMapping("/modifyFileContent")
    public ResponseEntity<Object> modifyFileContent(@Validated @RequestBody ConfigFile.ModifyFileContentArgs args) {
        configFileService.modifyFileContent(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> upload(@RequestParam("appId") Long appId, @RequestParam("envCode") String envCode,
                                         @RequestParam("file") MultipartFile file) throws Exception {
        configFileService.upload(appId, envCode, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@Validated @RequestBody ConfigFile.RemoveArgs args) {
        configFileService.remove(args);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
