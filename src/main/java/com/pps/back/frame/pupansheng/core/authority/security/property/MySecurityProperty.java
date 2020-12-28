package com.pps.back.frame.pupansheng.core.authority.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @discription;
 * @time 2020/5/14 13:47
 */

@Configuration
@ConfigurationProperties(prefix = "mysecurity")
public class MySecurityProperty {


    private  String [] permitUrl;
    private  Boolean openVerifyCode=false;
    private  Boolean openRequestLog=true;// 是否需要打印网络请求日志
    private  String  verifyCodeUrl="/verifyCode";
    private  String  loginUrl;
    private  String  logoutUrl;
    private  String  failureUrl;
    private  final   SmsProperty sms=new SmsProperty();
    private  String validateCodeParam="verifyCode";//验证码参数名
    private  Boolean canCrossOrigin=false;
    private  Boolean openConfigUser=false;
    private  String  configUser;

    public static class SmsProperty{

        private Boolean openSms=false;
        private  String  smsLoginUrl;//短信登录路径
        private  String  smsMessage;//获取短信验证码路径

        private  String  mobileParameter="mobile";//网络请求的电话号码

        private  String codeParameter="smsCode";//网络请求里面的验证码

        private  String sessionParam="codeParam";//产生验证码时存到session的参数名

        private  String sessionPhoneParam="mobile";//存到session里面的应该为一个map  这是map的电话号码参数

        private  String sessionCodeParam="code";//存到session里面的应该为一个map  这是map的验证码参数

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

        public Boolean getOpenSms() {
            return openSms;
        }

        public void setOpenSms(Boolean openSms) {
            this.openSms = openSms;
        }

        public String getSmsLoginUrl() {
            return smsLoginUrl;
        }

        public void setSmsLoginUrl(String smsLoginUrl) {
            this.smsLoginUrl = smsLoginUrl;
        }

        public String getSmsMessage() {
            return smsMessage;
        }

        public void setSmsMessage(String smsMessage) {
            this.smsMessage = smsMessage;
        }

        public String getMobileParameter() {
            return mobileParameter;
        }

        public void setMobileParameter(String mobileParameter) {
            this.mobileParameter = mobileParameter;
        }
    }

    public void addPrefix(String prefix){




    }

    public Boolean getOpenConfigUser() {
        return openConfigUser;
    }

    public void setOpenConfigUser(Boolean openConfigUser) {
        this.openConfigUser = openConfigUser;
    }

    public String getConfigUser() {
        return configUser;
    }

    public void setConfigUser(String configUser) {
        this.configUser = configUser;
    }

    public Boolean getCanCrossOrigin() {
        return canCrossOrigin;
    }

    public void setCanCrossOrigin(Boolean canCrossOrigin) {
        this.canCrossOrigin = canCrossOrigin;
    }

    public String getValidateCodeParam() {
        return validateCodeParam;
    }

    public void setValidateCodeParam(String validateCodeParam) {
        this.validateCodeParam = validateCodeParam;
    }

    public SmsProperty getSms() {
        return sms;
    }


    public Boolean getOpenRequestLog() {
        return openRequestLog;
    }

    public void setOpenRequestLog(Boolean openRequestLog) {
        this.openRequestLog = openRequestLog;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String[] getPermitUrl() {
        return permitUrl;
    }

    public void setPermitUrl(String[] permitUrl) {
        this.permitUrl = permitUrl;
    }

    public Boolean getOpenVerifyCode() {
        return openVerifyCode;
    }

    public void setOpenVerifyCode(Boolean openVerifyCode) {
        this.openVerifyCode = openVerifyCode;
    }

    public String getVerifyCodeUrl() {
        return verifyCodeUrl;
    }

    public void setVerifyCodeUrl(String verifyCodeUrl) {
        this.verifyCodeUrl = verifyCodeUrl;
    }
}
