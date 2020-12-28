package com.pps.back.frame.pupansheng.core.authority.security.component.handler;


import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import com.pps.back.frame.pupansheng.core.authority.security.property.JWTUtil;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
 * @discription;登陆成功处理
 * @time 2020/5/14 10:23
 */
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private  String type;

    private JWTUtil jwtUtil;

    private MySecurityProperty mySecurityProperty;

    private BiFunction<Object,Map,Map> function;

    public MyAuthenticationSuccessHandler(MySecurityProperty mySecurityProperty,JWTUtil jwtUtil) {
        this.jwtUtil=jwtUtil;
        this.mySecurityProperty=mySecurityProperty;
        this.type="表单";
        function=(o,m)->{
            m.put("data",o);
            return  m;
        };
    }

    public MyAuthenticationSuccessHandler(String type,MySecurityProperty mySecurityProperty,JWTUtil jwtUtil,BiFunction<Object,Map,Map> function) {
        this.jwtUtil=jwtUtil;
        this.mySecurityProperty=mySecurityProperty;
        this.type = type;
        this.function=function;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {


        log.info("登陆成功----------{}方式",this.type);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Map map=new HashMap<>();
        map.put("code", GlobalCode.Success.getTypeCode());
        map.put("status",true);
        map= function.apply(authentication,map);
        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();


    }
}
