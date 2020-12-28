package com.pps.back.frame.pupansheng.core.authority.security.component.handler;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @discription 未登录访问受限资源 的行为
 * @time 2020/5/14 17:26
 */
@Slf4j
public class NoAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {


        log.info("未登录或者权限不足-------！");
        e.printStackTrace();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Map map=new HashMap<>();
        map.put("code", GlobalCode.AuthorizationError.getTypeCode());
        map.put("status",false);
        map.put("error","未登录，访问保护资源,拒绝");

        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();



    }


}
