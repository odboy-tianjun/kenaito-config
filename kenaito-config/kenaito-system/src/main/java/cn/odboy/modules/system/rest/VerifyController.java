package cn.odboy.modules.system.rest;

import cn.odboy.constant.CodeBiEnum;
import cn.odboy.constant.CodeEnum;
import cn.odboy.constant.EmailTypeEnum;
import cn.odboy.domain.EmailLog;
import cn.odboy.modules.system.service.VerifyService;
import cn.odboy.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 系统：验证码管理
 * @author odboy
 * @date 2024-12-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class VerifyController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    /**
     * 重置邮箱，发送验证码
     */
    @PostMapping(value = "/resetEmail")
    public ResponseEntity<Object> resetEmail(@RequestParam String email) {
        EmailLog.SendContent emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(EmailTypeEnum.RESET_PASSWORD_CAPTCHA, emailVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * 重置密码，发送验证码
     */
    @PostMapping(value = "/email/resetPass")
    public ResponseEntity<Object> resetPass(@RequestParam String email) {
        EmailLog.SendContent emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(EmailTypeEnum.RESET_PASSWORD_CAPTCHA, emailVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * 验证码验证
     */
    @GetMapping(value = "/validated")
    public ResponseEntity<Object> validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
