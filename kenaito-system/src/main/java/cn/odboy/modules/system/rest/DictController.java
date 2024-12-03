package cn.odboy.modules.system.rest;

import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.system.domain.Dict;
import cn.odboy.modules.system.domain.vo.DictQueryCriteria;
import cn.odboy.modules.system.service.DictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 系统：字典管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;
    private static final String ENTITY_NAME = "dict";

    /**
     * 导出字典数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void exportDict(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        dictService.download(dictService.queryAll(criteria), response);
    }
    /**
     * 查询字典
     */
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<List<Dict>> queryAllDict() {
        return new ResponseEntity<>(dictService.queryAll(new DictQueryCriteria()), HttpStatus.OK);
    }
    /**
     * 查询字典
     */
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<PageResult<Dict>> queryDict(DictQueryCriteria resources, Page<Object> page) {
        return new ResponseEntity<>(dictService.queryAll(resources, page), HttpStatus.OK);
    }
    /**
     * 新增字典
     */
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Object> createDict(@Validated @RequestBody Dict resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        dictService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * 修改字典
     */
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Object> updateDict(@Validated(Dict.Update.class) @RequestBody Dict resources) {
        dictService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * 删除字典
     */
    @DeleteMapping
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Object> deleteDict(@RequestBody Set<Long> ids) {
        dictService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}