package cn.odboy.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.odboy.config.FileProperties;
import cn.odboy.constant.CacheKey;
import cn.odboy.infra.context.SecurityUtil;
import cn.odboy.infra.exception.EntityExistException;
import cn.odboy.infra.exception.EntityNotFoundException;
import cn.odboy.infra.redis.RedisUtil;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.response.PageUtil;
import cn.odboy.modules.security.repository.OnlineUserRepository;
import cn.odboy.modules.security.repository.UserCacheManager;
import cn.odboy.modules.system.domain.Job;
import cn.odboy.modules.system.domain.Role;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.domain.vo.UserQueryCriteria;
import cn.odboy.modules.system.mapper.UserJobMapper;
import cn.odboy.modules.system.mapper.UserMapper;
import cn.odboy.modules.system.mapper.UserRoleMapper;
import cn.odboy.modules.system.service.UserService;
import cn.odboy.util.MyFileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.BadRequestException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserJobMapper userJobMapper;
    private final UserRoleMapper userRoleMapper;
    private final FileProperties properties;
    private final RedisUtil redisUtils;
    private final UserCacheManager userCacheManager;
    private final OnlineUserRepository onlineUserRepository;

    @Override
    public PageResult<User> queryAll(UserQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<User> users = userMapper.findAll(criteria);
        Long total = userMapper.countAll(criteria);
        return PageUtil.toPage(users, total);
    }

    @Override
    public List<User> queryAll(UserQueryCriteria criteria) {
        return userMapper.findAll(criteria);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public User findById(long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(User resources) {
        resources.setDeptId(resources.getDept().getId());
        if (userMapper.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        if (userMapper.findByEmail(resources.getEmail()) != null) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (userMapper.findByPhone(resources.getPhone()) != null) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        save(resources);
        // 保存用户岗位
        userJobMapper.insertData(resources.getId(), resources.getJobs());
        // 保存用户角色
        userRoleMapper.insertData(resources.getId(), resources.getRoles());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) throws Exception {
        User user = getById(resources.getId());
        User user1 = userMapper.findByUsername(resources.getUsername());
        User user2 = userMapper.findByEmail(resources.getEmail());
        User user3 = userMapper.findByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (user3 != null && !user.getId().equals(user3.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        // 如果用户的角色改变
        if (!resources.getRoles().equals(user.getRoles())) {
            redisUtils.del(CacheKey.DATA_USER + resources.getId());
            redisUtils.del(CacheKey.MENU_USER + resources.getId());
            redisUtils.del(CacheKey.ROLE_AUTH + resources.getId());
        }
        // 修改部门会影响 数据权限
        if (!Objects.equals(resources.getDept(), user.getDept())) {
            redisUtils.del(CacheKey.DATA_USER + resources.getId());
        }
        // 如果用户被禁用，则清除用户登录信息
        if (!resources.getEnabled()) {
            onlineUserRepository.kickOutForUsername(resources.getUsername());
        }
        user.setDeptId(resources.getDept().getId());
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        user.setDept(resources.getDept());
        user.setJobs(resources.getJobs());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        saveOrUpdate(user);
        // 清除缓存
        delCaches(user.getId(), user.getUsername());
        // 更新用户岗位
        userJobMapper.deleteByUserId(resources.getId());
        userJobMapper.insertData(resources.getId(), resources.getJobs());
        // 更新用户角色
        userRoleMapper.deleteByUserId(resources.getId());
        userRoleMapper.insertData(resources.getId(), resources.getRoles());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(User resources) {
        User user = getById(resources.getId());
        User user1 = userMapper.findByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        saveOrUpdate(user);
        // 清理缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            User user = getById(id);
            delCaches(user.getId(), user.getUsername());
        }
        userMapper.deleteBatchIds(ids);
        // 删除用户岗位
        userJobMapper.deleteByUserIds(ids);
        // 删除用户角色
        userRoleMapper.deleteByUserIds(ids);
    }

    @Override
    public User findByName(String userName) {
        return userMapper.findByUsername(userName);
    }

    @Override
    public User getLoginData(String userName) {
        User user = userMapper.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return user;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userMapper.updatePass(username, pass, new Date());
        flushCache(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(Set<Long> ids, String pwd) {
        userMapper.resetPwd(ids, pwd);
    }

    @Override
    public PageResult<User> pageList(UserQueryCriteria body, Page<User> page) {
        if (page.getSize() > 20) {
            page.setSize(50);
        }
        if (StrUtil.isBlank(body.getBlurry())) {
            Page<User> userPage = userMapper.selectPage(page, new LambdaQueryWrapper<User>()
                    .orderByDesc(User::getId)
                    .eq(User::getEnabled, 1)
            );
            return new PageResult<>(userPage.getRecords(), userPage.getTotal());
        }
        Page<User> userPage = userMapper.selectPage(page, new LambdaQueryWrapper<User>()
                .eq(User::getEnabled, 1)
                .or(c -> c.like(User::getEmail, body.getBlurry())
                        .or()
                        .like(User::getUsername, body.getBlurry())
                        .or()
                        .like(User::getNickName, body.getBlurry())
                )
                .orderByDesc(User::getId)
        );
        return new PageResult<>(userPage.getRecords(), userPage.getTotal());
    }

    @Override
    public User getUserByPhome(String mobile) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEnabled, 1)
                .eq(User::getPhone, mobile)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        // 文件大小验证
        MyFileUtil.checkSize(properties.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = MyFileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        User user = userMapper.findByUsername(SecurityUtil.getCurrentUsername());
        String oldPath = user.getAvatarPath();
        File file = MyFileUtil.upload(multipartFile, properties.getPath().getAvatar());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        saveOrUpdate(user);
        if (StringUtil.isNotBlank(oldPath)) {
            MyFileUtil.del(oldPath);
        }
        @NotBlank String username = user.getUsername();
        flushCache(username);
        return new HashMap<String, String>(1) {{
            put("avatar", file.getName());
        }};
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userMapper.updateEmail(username, email);
        flushCache(username);
    }

    @Override
    public void download(List<User> users, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (User user : users) {
            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUsername());
            map.put("角色", roles);
            map.put("部门", user.getDept().getName());
            map.put("岗位", user.getJobs().stream().map(Job::getName).collect(Collectors.toList()));
            map.put("邮箱", user.getEmail());
            map.put("状态", user.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", user.getPhone());
            map.put("修改密码的时间", user.getPwdResetTime());
            map.put("创建日期", user.getCreateTime());
            list.add(map);
        }
        MyFileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, String username) {
        redisUtils.del(CacheKey.USER_ID + id);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheManager.cleanUserCache(username);
    }
}
