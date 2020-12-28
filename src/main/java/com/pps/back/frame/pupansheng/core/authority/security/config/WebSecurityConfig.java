package com.pps.back.frame.pupansheng.core.authority.security.config;

import com.alibaba.fastjson.JSON;

import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import com.pps.back.frame.pupansheng.core.authority.security.component.common.CustomAuthenticationDetailsSource;
import com.pps.back.frame.pupansheng.core.authority.security.component.common.CustomPermissionEvaluator;
import com.pps.back.frame.pupansheng.core.authority.security.component.common.MyInvalidSessionStrategy;
import com.pps.back.frame.pupansheng.core.authority.security.component.filter.CrossOriginFilter;
import com.pps.back.frame.pupansheng.core.authority.security.component.filter.RequestInfoFilter;
import com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.form.CustomAuthenticationProviderForPc;
import com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.jwt.JwtAuthenticationFilter;
import com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.sms.SmsCodeAuthenticationSecurityConfig;
import com.pps.back.frame.pupansheng.core.authority.security.property.JWTUtil;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import com.pps.back.frame.pupansheng.core.authority.security.component.handler.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author
 * @discription;
 * @time 2020/5/13 16:58
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProviderForPc customAuthenticationProviderForPc;
    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;
    @Autowired
    private MySecurityProperty mySecurityProperty;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    Environment environment;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    /**
     * 注入自定义PermissionEvaluator  页面级别权限控制
     */
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(customAuthenticationProviderForPc);//授权认证器

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String context=environment.getProperty("server.servlet.context-path");
        log.info("------------------------------------------------------------------------------------------------");
        log.info("web应用的上下文为{}",context);
        log.info("------------------------------------------------------------------------------------------------");
        String [] permitUrl = mySecurityProperty.getPermitUrl();
        Boolean openVerifyCode = mySecurityProperty.getOpenVerifyCode();

        if(openVerifyCode){

            if(ValidateUtil.isNotEmpty(mySecurityProperty.getVerifyCodeUrl())){
                List<String> newUrl=new ArrayList<>( Arrays.asList(permitUrl));
                newUrl.add(mySecurityProperty.getVerifyCodeUrl());
                permitUrl=newUrl.toArray(new String[newUrl.size()]);
            }else {

                throw new RuntimeException("已开启验证码登录  但是没有配置验证码获取路径");
            }
        }

        if(mySecurityProperty.getSms()!=null&&ValidateUtil.isNotEmpty(mySecurityProperty.getSms().getSmsMessage())){
            List<String> newUrl=new ArrayList<>( Arrays.asList(permitUrl));
            newUrl.add(mySecurityProperty.getSms().getSmsMessage());
            permitUrl=newUrl.toArray(new String[newUrl.size()]);
        }


        log.info("配置地址信息---------------------------------------------------------------------------------------------");
                log.info("已开启springSecurity: ");
                log.info("不限制权限的url如下：----------------------------------------------------------------------------");
                for(String u:permitUrl) {
                    log.info(u);
                }
                log.info("-----------------------------------------------------------------------------------------------");
                log.info("登录地址为：{}",mySecurityProperty.getLoginUrl());
                log.info("退出地址为：{}", mySecurityProperty.getLogoutUrl());
                log.info("出错访问的地址为：{}",mySecurityProperty.getFailureUrl());


                //表单登录成功逻辑 o为Authentication
                BiFunction<Object, Map,Map> formSuccessfunction=(o,m)->{

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
                //表单登录失败逻辑 o为Authentication
                BiFunction<Object, Map,Map> formFailfunction=(o,m)->{

                  m.put("data",o);
                  return  m;

               };
                BiConsumer<HttpServletRequest, HttpServletResponse> loginOutDone=(res, response)->{

                 log.info("对登录退出没有太多处理");


               };


        http.apply(smsCodeAuthenticationSecurityConfig).and().
                 authorizeRequests()
                // 如果有允许匿名的url，填在下面
                .antMatchers(permitUrl).permitAll()
                //除此之外都需要登陆权限
                .anyRequest().authenticated().and()
                //自己定义的 customAuthenticationDetail
                .formLogin().authenticationDetailsSource(customAuthenticationDetailsSource)
                //登录地址 和登陆成功的自定义处理类
                .loginPage(mySecurityProperty.getLoginUrl()).loginProcessingUrl(mySecurityProperty.getLoginUrl()).successHandler(new MyAuthenticationSuccessHandler("表单",mySecurityProperty,jwtUtil,formSuccessfunction))
                //登陆失败的自定义处理类
                .failureUrl(mySecurityProperty.getFailureUrl()).failureHandler(new MyAuthentionFailHandler("表单")).and()
                //登陆退出自定义
                .logout().logoutUrl(mySecurityProperty.getLogoutUrl()).logoutSuccessHandler(new MyLogoutSuccessHandler()).deleteCookies("JSESSIONID").and()
                //错误处理自定义类 分别为   前者 没有授权  后者权限不足
                .exceptionHandling().authenticationEntryPoint(new NoAuthenticationEntryPoint()).accessDeniedHandler(new MyAccessDenHandler()).and()
                //session失效处理自定义类
                .sessionManagement().invalidSessionStrategy(new MyInvalidSessionStrategy());

        //自定义的拦截器  用于打印网络请求信息
        if(mySecurityProperty.getOpenRequestLog()) {

            log.info("已开启网络请求打印过滤： 系统生成网络请求打印日志过滤器");
            http.addFilterBefore(new RequestInfoFilter(), SecurityContextPersistenceFilter.class);
        }

        if(jwtUtil.isEnable()) {
            log.info("已开启jwt登录： 系统生成jwt过滤器");
            JwtAuthenticationFilter jwtAuthenticationFilter=new JwtAuthenticationFilter();
            jwtAuthenticationFilter.setJwtUtil(jwtUtil);
            jwtAuthenticationFilter.setMySecurityProperty(mySecurityProperty);
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }
        if(mySecurityProperty.getCanCrossOrigin()) {
            log.info("已开启跨域处理： 系统生成跨域处理过滤器");
            CrossOriginFilter crossOriginFilter=new CrossOriginFilter();
            http.addFilterBefore(crossOriginFilter, SecurityContextPersistenceFilter.class);
        }
        // 关闭CSRF跨域
        http.csrf().disable();
        log.info("security配置结束--------------------------------------------------------------------------------------------");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }



}
