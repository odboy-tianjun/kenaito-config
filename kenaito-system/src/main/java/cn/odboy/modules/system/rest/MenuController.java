package cn.odboy.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.infra.context.SecurityUtil;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.response.PageUtil;
import cn.odboy.modules.system.domain.Menu;
import cn.odboy.modules.system.domain.vo.MenuQueryCriteria;
import cn.odboy.modules.system.domain.vo.MenuVo;
import cn.odboy.modules.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 系统：菜单管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuService menuService;
    private static final String ENTITY_NAME = "menu";

    /**
     * 导出菜单数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void exportMenu(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }
    /**
     * 获取前端所需菜单
     */
    @GetMapping(value = "/build")
    public ResponseEntity<List<MenuVo>> buildMenus() {
        List<Menu> menuList = menuService.findByUser(SecurityUtil.getCurrentUserId());
        List<Menu> menus = menuService.buildTree(menuList);
        return new ResponseEntity<>(menuService.buildMenus(menus), HttpStatus.OK);
    }
    /**
     * 返回全部的菜单
     */
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<List<Menu>> queryAllMenu(@RequestParam Long pid) {
        return new ResponseEntity<>(menuService.getMenus(pid), HttpStatus.OK);
    }
    /**
     * 根据菜单ID返回所有子节点ID，包含自身ID
     */
    @GetMapping(value = "/child")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> childMenu(@RequestParam Long id) {
        Set<Menu> menuSet = new HashSet<>();
        List<Menu> menuList = menuService.getMenus(id);
        menuSet.add(menuService.getById(id));
        menuSet = menuService.getChildMenus(menuList, menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }
    /**
     * 查询菜单
     */
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<PageResult<Menu>> queryMenu(MenuQueryCriteria criteria) throws Exception {
        List<Menu> menuList = menuService.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(menuList), HttpStatus.OK);
    }
    /**
     * 查询菜单:根据ID获取同级与上级数据
     */
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<List<Menu>> getMenuSuperior(@RequestBody List<Long> ids) {
        Set<Menu> menus = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                Menu menu = menuService.findById(id);
                List<Menu> menuList = menuService.getSuperior(menu, new ArrayList<>());
                for (Menu data : menuList) {
                    if (data.getId().equals(menu.getPid())) {
                        data.setSubCount(data.getSubCount() - 1);
                    }
                }
                menus.addAll(menuList);
            }
            // 编辑菜单时不显示自己以及自己下级的数据，避免出现PID数据环形问题
            menus = menus.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
            return new ResponseEntity<>(menuService.buildTree(new ArrayList<>(menus)), HttpStatus.OK);
        }
        return new ResponseEntity<>(menuService.getMenus(null), HttpStatus.OK);
    }
    /**
     * 新增菜单
     */
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<Object> createMenu(@Validated @RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        menuService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * 修改菜单
     */
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<Object> updateMenu(@Validated(Menu.Update.class) @RequestBody Menu resources) {
        menuService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * 删除菜单
     */
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<Object> deleteMenu(@RequestBody Set<Long> ids) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<Menu> menuList = menuService.getMenus(id);
            menuSet.add(menuService.getById(id));
            menuSet = menuService.getChildMenus(menuList, menuSet);
        }
        menuService.delete(menuSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
