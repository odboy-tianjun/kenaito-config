package cn.odboy.modules.system.rest;

import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.system.domain.Job;
import cn.odboy.modules.system.domain.vo.JobQueryCriteria;
import cn.odboy.modules.system.service.JobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
/**
 * 系统：岗位管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;
    private static final String ENTITY_NAME = "job";

    /**
     * 导出岗位数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void exportJob(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        jobService.download(jobService.queryAll(criteria), response);
    }
    /**
     * 查询岗位
     */
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<PageResult<Job>> queryJob(JobQueryCriteria criteria, Page<Object> page) {
        return new ResponseEntity<>(jobService.queryAll(criteria, page), HttpStatus.OK);
    }
    /**
     * 新增岗位
     */
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Object> createJob(@Validated @RequestBody Job resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        jobService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * 修改岗位
     */
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Object> updateJob(@Validated(Job.Update.class) @RequestBody Job resources) {
        jobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * 删除岗位
     */
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Object> deleteJob(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}