package cn.odboy.rest;

import cn.odboy.constant.EmailTypeEnum;
import cn.odboy.domain.EmailLog;
import cn.odboy.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工具：邮件管理
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/email")
public class EmailController {

    private final EmailService emailService;

    /**
     * 发送邮件
     */
    @PostMapping
    public ResponseEntity<Object> sendEmail(@Validated @RequestBody EmailLog.SendContent request) {
        emailService.send(EmailTypeEnum.TEST, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
