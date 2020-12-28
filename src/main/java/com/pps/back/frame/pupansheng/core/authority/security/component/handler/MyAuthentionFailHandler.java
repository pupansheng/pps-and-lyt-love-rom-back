package com.pps.back.frame.pupansheng.core.authority.security.component.handler;


import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author
 * @discription;认证失败处理
 * @time 2020/5/16 18:51
 */
/*
 登陆失败处理逻辑
 */
@Slf4j
public class MyAuthentionFailHandler implements AuthenticationFailureHandler {

    private BiFunction<Object,Map,Map> function;

    public  String type;

    public  MyAuthentionFailHandler(){

        this.type="表单";
        function=(o,m)->{
            AuthenticationException e=(AuthenticationException)o;
            m.put("error","登陆错误（表单）："+e.getMessage());
            return  m;

        };

    }
    public MyAuthentionFailHandler(String type) {

        this.type = type;
        function=(o,m)->{
            AuthenticationException e=(AuthenticationException)o;
            m.put("error","登陆错误（表单）："+e.getMessage());
            return  m;

        };


    }
    public MyAuthentionFailHandler(String type,BiFunction<Object,Map,Map> function) {

        this.type = type;
        this.function=function;

    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Map map=new HashMap<>();
        map.put("code", GlobalCode.AuthorizationError.getTypeCode());
        map.put("status",false);


        log.info("登陆失败----------{}登录",this.type);

         map=function.apply(exception,map);

        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();


    }
}

