package com.pps.back.frame.pupansheng.core.authority.security.component.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @discription; 多端登录 下线处理  一般不做限制  若要限制 则需在securiyConfig 加入如下代码
 *
 * .sessionManagement()
 * 	                    .invalidSessionUrl("/login/invalid")
 *                  	.maximumSessions(1)
 * 	                    // 当达到最大值时，是否保留已经登录的用户
 * 	                    .maxSessionsPreventsLogin(false)
 *                   	// 当达到最大值时，旧用户被踢出后的操作
 *                      .expiredSessionStrategy(new CustomExpiredSessionStrategy())
 * @time 2020/5/15 19:30
 */
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    private ObjectMapper objectMapper = new ObjectMapper();
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", GlobalCode.AuthorizationError.getTypeCode());
        map.put("status",false);
        map.put("msg", "已经另一台机器登录，您被迫下线。" + event.getSessionInformation().getLastRequest());
        // Map -> Json
        String json = objectMapper.writeValueAsString(map);

        event.getResponse().setContentType("application/json;charset=UTF-8");
        event.getResponse().getWriter().write(json);

        // 如果是跳转html页面，url代表跳转的地址
        // redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), "url");
    }
}
