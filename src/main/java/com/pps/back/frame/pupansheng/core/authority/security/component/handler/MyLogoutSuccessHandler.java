package com.pps.back.frame.pupansheng.core.authority.security.component.handler;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author
 * @discription;退出登录处理
 * @time 2020/5/14 10:26
 */
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    BiConsumer<HttpServletRequest,HttpServletResponse> loginOutDone=(res,response)->{



    };

    public MyLogoutSuccessHandler() {

    }

    public MyLogoutSuccessHandler(BiConsumer<HttpServletRequest, HttpServletResponse> loginOutDone) {
        this.loginOutDone = loginOutDone;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        loginOutDone.accept(httpServletRequest,httpServletResponse);
        Map map=new HashMap<>();
        map.put("code", GlobalCode.Success.getTypeCode());
        map.put("status",true);
        map.put("data","退出登录成功");
        log.info(new Date()+":退出成功:用户："+authentication);
        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();

    }
}
