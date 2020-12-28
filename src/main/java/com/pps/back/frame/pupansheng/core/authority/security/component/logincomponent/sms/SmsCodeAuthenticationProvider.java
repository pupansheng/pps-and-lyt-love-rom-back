package com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 短信登陆鉴权 Provider，要求实现 AuthenticationProvider 接口
 * @author jitwxs
 * @since 2019/1/9 13:59
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private  String codeParameter="smsCode";//网络请求里面的验证码

    private  String sessionParam="codeParam";//产生验证码时存到session的参数明

    private  String sessionPhoneParam="mobile";//存到session里面的应该为一个map  这是map的电话号码

    private  String sessionCodeParam="code";//存到session里面的应该为一个map  这是map的验证码

    public String getCodeParameter() {
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    public String getSessionParam() {
        return sessionParam;
    }

    public void setSessionParam(String sessionParam) {
        this.sessionParam = sessionParam;
    }

    public String getSessionPhoneParam() {
        return sessionPhoneParam;
    }

    public void setSessionPhoneParam(String sessionPhoneParam) {
        this.sessionPhoneParam = sessionPhoneParam;
    }

    public String getSessionCodeParam() {
        return sessionCodeParam;
    }

    public void setSessionCodeParam(String sessionCodeParam) {
        this.sessionCodeParam = sessionCodeParam;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();

        checkSmsCode(mobile);

        CustomUserDetailsServiceForSms customUserDetailsServiceForSms=(CustomUserDetailsServiceForSms)userDetailsService;

        UserDetails userDetails =customUserDetailsServiceForSms.loadUserByPhone(mobile);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails.getUsername(), userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    private void checkSmsCode(String mobile) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String inputCode = request.getParameter(codeParameter);

        Map<String, Object> smsCode = (Map<String, Object>) request.getSession().getAttribute(sessionParam);
        if(smsCode == null) {
            throw new BadCredentialsException("未检测到申请验证码");
        }

        String applyMobile = (String) smsCode.get(sessionPhoneParam);
        int code = (int) smsCode.get(sessionCodeParam);

        if(!applyMobile.equals(mobile)) {
            throw new BadCredentialsException("申请的手机号码与登录手机号码不一致");
        }
        if(code != Integer.parseInt(inputCode)) {
            throw new BadCredentialsException("验证码错误");
        }
    }

    @Override
    public boolean supports(Class<?> token) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return SmsCodeAuthenticationToken.class.isAssignableFrom(token);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
