package com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.form;


import com.pps.back.frame.pupansheng.core.authority.security.component.common.CustomWebAuthenticationDetails;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*
  pc 端登录所用的登陆判断逻辑器
 */
@Component
@Slf4j
public class CustomAuthenticationProviderForPc implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsServiceForPc customUserDetailsServiceForPc;
    @Autowired
    private MySecurityProperty mySecurityProperty;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String inputName = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();

        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();

        if(mySecurityProperty.getOpenVerifyCode()) {

            String verifyCode = details.getVerifyCode();
            if (!validateVerify(verifyCode)) {

                throw new DisabledException("验证码输入错误");

            }

        }

        // userDetails为数据库中查询到的用户信息
        UserDetails userDetails = customUserDetailsServiceForPc.loadUserByUsername(inputName);

        // 如果是自定义AuthenticationProvider，需要手动密码校验
        if(!userDetails.getPassword().equals(inputPassword)) {
            throw new BadCredentialsException("密码错误");
        }

        return new UsernamePasswordAuthenticationToken(inputName, inputPassword, userDetails.getAuthorities());
    }


    private boolean validateVerify(String inputVerify) {
        //获取当前线程绑定的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 不分区大小写
        // 这个validateCode是在servlet中存入session的名字
        String validateCode = ((String) request.getSession().getAttribute(mySecurityProperty.getValidateCodeParam())).toLowerCase();
        inputVerify = inputVerify.toLowerCase();
        log.info("验证码：" + validateCode + "用户输入：" + inputVerify);

        return validateCode.equals(inputVerify);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 这里不要忘记，和UsernamePasswordAuthenticationToken比较  这是pc manager器选择的依据

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
