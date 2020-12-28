package com.pps.back.frame.pupansheng.core.authority.security.component.common;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @discription;登录过期处理
 * @time 2020/5/14 23:55
 */
@Slf4j
public class MyInvalidSessionStrategy implements InvalidSessionStrategy {


    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        log.info("登录过期！");

        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Map map=new HashMap<>();
        map.put("code", GlobalCode.AuthorizationError.getTypeCode());
        map.put("status",false);
        map.put("error","登录过期，请重新登录");

        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();



    }
}
