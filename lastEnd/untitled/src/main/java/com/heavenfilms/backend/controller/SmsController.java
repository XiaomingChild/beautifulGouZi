package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@CrossOrigin
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/send")
    public Result<String> send(@RequestParam String phone) {
        if (phone == null || phone.length() != 11) {
            return Result.error("手机号格式错误");
        }
        boolean success = smsService.sendVerifyCode(phone);
        return success ? Result.success("发送成功") : Result.error("发送失败，请检查配置或稍后再试");
    }
}
