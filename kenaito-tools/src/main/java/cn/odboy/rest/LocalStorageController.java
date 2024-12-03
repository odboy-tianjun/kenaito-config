package cn.odboy.rest;

import cn.odboy.domain.LocalStorage;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.service.LocalStorageService;
import cn.odboy.util.MyFileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 工具：本地存储管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;

    /**
     * 查询文件
     */
    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<Object> queryFile(LocalStorage.QueryArgs criteria, Page<Object> page) {
        return new ResponseEntity<>(localStorageService.queryAll(criteria, page), HttpStatus.OK);
    }
    /**
     * 导出数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, LocalStorage.QueryArgs criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }
    /**
     * 上传文件
     */
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Object> createFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        localStorageService.create(name, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * 上传图片
     */
    @PostMapping("/pictures")
    public ResponseEntity<Object> uploadPicture(@RequestParam MultipartFile file) {
        // 判断文件是否为图片
        String suffix = MyFileUtil.getExtensionName(file.getOriginalFilename());
        if (!MyFileUtil.IMAGE.equals(MyFileUtil.getFileType(suffix))) {
            throw new BadRequestException("只能上传图片");
        }
        LocalStorage localStorage = localStorageService.create(null, file);
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
    }
    /**
     * 修改文件
     */
    @PutMapping
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity<Object> updateFile(@Validated @RequestBody LocalStorage resources) {
        localStorageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * 删除文件
     */
    @DeleteMapping
    public ResponseEntity<Object> deleteFile(@RequestBody Long[] ids) {
        localStorageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
