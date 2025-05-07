package cn.odboy.modules.security.rest;

import cn.odboy.modules.security.repository.OnlineUserRepository;
import cn.odboy.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 系统：在线用户管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
public class OnlineController {

    private final OnlineUserRepository onlineUserRepository;

    /**
     * 查询在线用户
     */
    @GetMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> queryOnlineUser(String username, Pageable pageable) {
        return new ResponseEntity<>(onlineUserRepository.getAll(username, pageable), HttpStatus.OK);
    }
    /**
     * 导出数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
        onlineUserRepository.download(onlineUserRepository.getAll(username), response);
    }
    /**
     * 踢出用户
     */
    @DeleteMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> deleteOnlineUser(@RequestBody Set<String> keys) throws Exception {
        for (String token : keys) {
            // 解密Key
            token = EncryptUtil.desDecrypt(token);
            onlineUserRepository.logout(token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
