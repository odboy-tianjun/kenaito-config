package cn.odboy.modules.system.rest;

import cn.hutool.core.lang.Dict;
import cn.odboy.infra.context.SecurityUtil;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.system.domain.Role;
import cn.odboy.modules.system.domain.vo.RoleQueryCriteria;
import cn.odboy.modules.system.service.RoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统：角色管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    private static final String ENTITY_NAME = "role";

    /**
     * 获取单个role
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Role> findRoleById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }
    /**
     * 导出角色数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }
    /**
     * 返回全部的角色
     */
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<List<Role>> queryAllRole() {
        return new ResponseEntity<>(roleService.queryAll(), HttpStatus.OK);
    }
    /**
     * 查询角色
     */
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<PageResult<Role>> queryRole(RoleQueryCriteria criteria, Page<Object> page) {
        return new ResponseEntity<>(roleService.queryAll(criteria, page), HttpStatus.OK);
    }
    /**
     * 获取用户级别
     */
    @GetMapping(value = "/level")
    public ResponseEntity<Object> getRoleLevel() {
        return new ResponseEntity<>(Dict.create().set("level", getLevels(null)), HttpStatus.OK);
    }
    /**
     * 新增角色
     */
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Object> createRole(@Validated @RequestBody Role resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        getLevels(resources.getLevel());
        roleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * 修改角色
     */
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updateRole(@Validated(Role.Update.class) @RequestBody Role resources) {
        getLevels(resources.getLevel());
        roleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * 修改角色菜单
     */
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updateRoleMenu(@RequestBody Role resources) {
        Role role = roleService.getById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * 删除角色
     */
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Object> deleteRole(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Role role = roleService.getById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 获取用户的角色级别
     */
    private int getLevels(Integer level) {
        List<Integer> levels = roleService.findByUsersId(SecurityUtil.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
