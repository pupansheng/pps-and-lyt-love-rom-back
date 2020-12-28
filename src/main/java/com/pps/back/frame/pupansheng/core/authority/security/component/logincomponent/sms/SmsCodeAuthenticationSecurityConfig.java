package com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.sms;


import com.alibaba.fastjson.JSON;

import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import com.pps.back.frame.pupansheng.core.authority.security.component.handler.MyAuthenticationSuccessHandler;
import com.pps.back.frame.pupansheng.core.authority.security.component.handler.MyAuthentionFailHandler;
import com.pps.back.frame.pupansheng.core.authority.security.property.JWTUtil;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
@Slf4j
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private CustomUserDetailsServiceForSms customUserDetailsServiceForSms;
    @Autowired
    private MySecurityProperty mySecurityProperty;
    @Autowired
    private JWTUtil jwtUtil;


    @Override
    public void configure(HttpSecurity http) throws Exception {

             MySecurityProperty.SmsProperty sms = mySecurityProperty.getSms();

           if(sms!=null&&sms.getOpenSms()){

            if(ValidateUtil.isEmpty(sms.getSmsMessage())){

                throw new RuntimeException("开启了短信登录 但是没有配置短信验证码获取路径");
            }

            SmsCodeAuthenticationFilter smsCodeAuthenticationFilter=null;
            if(ValidateUtil.isNotEmpty(sms.getSmsLoginUrl())) {
                smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter(sms.getSmsLoginUrl());
            }else {
                log.info("没有配置短信登录的路径  默认路径为/sms/login");
                smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
            }

                 //表单登录成功逻辑 o为Authentication
                 BiFunction<Object, Map,Map> formSuccessfunction=(o, m)->{
                     if(jwtUtil.isEnable()){
                         log.info("已开启jwt登录处理：开始生成token");
                         Authentication authentication=(Authentication)o;
                         Map response=new HashMap();
                         response.put("userName",authentication.getPrincipal());
                         response.put("authList",authentication.getAuthorities());
                         String token = jwtUtil.generateToken(JSON.toJSONString(response));
                         log.info("token包含信息："+ JSON.toJSONString(response));
                         log.info("产生token:"+token);
                         m.put("token",token);
                     }else {
                         m.put("data",o);
                     }

                     return  m;

                 };

            smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
            smsCodeAuthenticationFilter.setMobileParameter(sms.getMobileParameter());
            smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler("短信",mySecurityProperty,jwtUtil,formSuccessfunction));
            smsCodeAuthenticationFilter.setAuthenticationFailureHandler(new MyAuthentionFailHandler("短信"));

            SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();

            smsCodeAuthenticationProvider.setCodeParameter(sms.getCodeParameter());
            smsCodeAuthenticationProvider.setSessionParam(sms.getSessionParam());
            smsCodeAuthenticationProvider.setSessionCodeParam(sms.getSessionCodeParam());
            smsCodeAuthenticationProvider.setSessionPhoneParam(sms.getSessionPhoneParam());

            smsCodeAuthenticationProvider.setUserDetailsService(customUserDetailsServiceForSms);
            http.authenticationProvider(smsCodeAuthenticationProvider)
                    .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                 log.info("已开启短信验证码登录：");
                 log.info("短信登录地址为：{}",sms.getSmsLoginUrl());
                 log.info("短信验证码获取地址为：{}",sms.getSmsMessage());
                 log.info("网络请求传电话号码参数名为：{}",sms.getMobileParameter());
                 log.info("网络请求传验证码参数名为：{}",sms.getCodeParameter());
                 log.info("产生验证码后 生成一个map 存到session里面 键值为：{}",sms.getSessionParam());
                 log.info("产生验证码后存到session里面map 电话键值为：{}   验证码键值为：{}",sms.getSessionPhoneParam(),sms.getSessionCodeParam());
        }



    }
}
