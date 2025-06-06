package cn.odboy.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.odboy.config.AppProperties;
import cn.odboy.infra.context.SecurityUtil;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.redis.RedisUtil;
import cn.odboy.infra.rest.AnonymousDeleteMapping;
import cn.odboy.infra.rest.AnonymousGetMapping;
import cn.odboy.infra.rest.AnonymousPostMapping;
import cn.odboy.modules.security.config.LoginProperties;
import cn.odboy.modules.security.config.SecurityProperties;
import cn.odboy.modules.security.constant.LoginCodeEnum;
import cn.odboy.modules.security.context.TokenProvider;
import cn.odboy.modules.security.domain.AuthUserDto;
import cn.odboy.modules.security.domain.JwtUserDto;
import cn.odboy.modules.security.repository.OnlineUserRepository;
import cn.odboy.util.RsaUtils;
import cn.odboy.util.StringUtil;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统：系统授权接口
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final SecurityProperties properties;
    private final RedisUtil redisUtil;
    private final OnlineUserRepository onlineUserRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LoginProperties loginProperties;

    /**
     * 用户登录
     */
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        Boolean enabled = loginProperties.getLoginCode().getEnabled();
        if (enabled) {
            // 查询验证码
            String code = (String) redisUtil.get(authUser.getUuid());
            // 清除验证码
            redisUtil.del(authUser.getUuid());
            if (StringUtil.isBlank(code)) {
                throw new BadRequestException("验证码不存在或已过期");
            }
            if (StringUtil.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
                throw new BadRequestException("验证码错误");
            }
        }
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(AppProperties.privateKey, authUser.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌与第三方系统获取令牌方式
        // UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserRepository.kickOutForUsername(authUser.getUsername());
        }
        // 保存在线信息
        onlineUserRepository.save(jwtUserDto, token, request);
        // 返回登录信息
        return ResponseEntity.ok(authInfo);
    }
    /**
     * 获取用户信息
     */
    @GetMapping(value = "/info")
    public ResponseEntity<UserDetails> getUserInfo() {
        return ResponseEntity.ok(SecurityUtil.getCurrentUser());
    }
    /**
     * 获取验证码
     */
    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        Boolean enabled = loginProperties.getLoginCode().getEnabled();
        if (enabled) {
            // 获取运算的结果
            Captcha captcha = loginProperties.getCaptcha();
            String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
            //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
            String captchaValue = captcha.text();
            String containsTag = ".";
            if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(containsTag)) {
                captchaValue = captchaValue.split("\\.")[0];
            }
            // 保存
            redisUtil.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
            // 验证码信息
            Map<String, Object> imgResult = new HashMap<>(2) {{
                put("img", captcha.toBase64());
                put("uuid", uuid);
                put("enabled", enabled);
            }};
            return ResponseEntity.ok(imgResult);
        } else {
            Map<String, Object> imgResult = new HashMap<>(2) {{
                put("img", "");
                put("uuid", "");
                put("enabled", enabled);
            }};
            return ResponseEntity.ok(imgResult);
        }
    }
    /**
     * 退出登录
     */
    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        onlineUserRepository.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
