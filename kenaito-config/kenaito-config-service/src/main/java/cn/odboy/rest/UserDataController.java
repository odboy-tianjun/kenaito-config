package cn.odboy.rest;

import cn.odboy.base.model.SelectOption;
import cn.odboy.infra.response.PageArgs;
import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.domain.vo.UserQueryCriteria;
import cn.odboy.modules.system.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 配置中心：用户信息
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/config/user")
public class UserDataController {
    private final UserService userService;

    /**
     * 分页查询用户列表
     */
    @PostMapping("/pageList")
    public ResponseEntity<Object> pageList(@RequestBody PageArgs<UserQueryCriteria> args) {
        PageResult<User> result = userService.pageList(args.getBody(), new Page<>(args.getPage(), args.getPageSize()));
        List<SelectOption> selectOptions = result.getContent().stream().map(m -> {
            SelectOption option = new SelectOption();
            option.setValue(String.valueOf(m.getId()));
            option.setLabel(m.getUsername());
            return option;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(selectOptions, HttpStatus.OK);
    }
}
