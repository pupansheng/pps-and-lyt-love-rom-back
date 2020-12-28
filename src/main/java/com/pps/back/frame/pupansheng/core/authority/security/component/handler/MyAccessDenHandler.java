package com.pps.back.frame.pupansheng.core.authority.security.component.handler;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @discription;权限不足处理
 * @time 2020/5/14 17:37
 */
@Slf4j
public class MyAccessDenHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        log.info("权限不足------MyAccessDenHandler！");

        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Map map=new HashMap<>();
        map.put("code", GlobalCode.AuthorizationError.getTypeCode());
        map.put("status",false);
        map.put("error","不支持当前登录者访问");

        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();

    }
}
