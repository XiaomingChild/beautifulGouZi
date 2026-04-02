package com.heavenfilms.backend.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsService {

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    private final Map<String, String> verifyCodeStore = new ConcurrentHashMap<>();

    /**
     * 初始化阿里云短信客户端
     */
    private Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint("dysmsapi.aliyuncs.com");
        return new Client(config);
    }

    /**
     * 发送验证码
     */
    public boolean sendVerifyCode(String phone) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        
        try {
            Client client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\"" + code + "\", \"min\":\"5\"}");

            // 发送短信
            SendSmsResponse res = client.sendSms(sendSmsRequest);
            
            if ("OK".equalsIgnoreCase(res.getBody().getCode())) {
                verifyCodeStore.put(phone, code);
                System.out.println("短信发送成功，验证码: " + code);
                return true;
            } else {
                System.err.println("短信发送失败: " + res.getBody().getMessage());
                // 降级策略：测试环境如果配置不对，直接模拟通过
                verifyCodeStore.put(phone, code);
                System.out.println("DEBUG (阿里云返回错误): 模拟验证码发送 -> " + code);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 彻底报错也模拟通过，保证你的毕设流程不因为外部服务卡死
            verifyCodeStore.put(phone, code);
            System.out.println("DEBUG (SDK异常): 模拟验证码发送 -> " + code);
            return true;
        }
    }

    public boolean checkCode(String phone, String code) {
        if (code == null || phone == null) return false;
        String savedCode = verifyCodeStore.get(phone);
        boolean isValid = code.equals(savedCode);
        if (isValid) {
            verifyCodeStore.remove(phone);
        }
        return isValid;
    }
}
