package com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.jwt;

import com.alibaba.fastjson.JSON;

import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import com.pps.back.frame.pupansheng.core.authority.security.property.JWTUtil;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @discription;提取 请求中的token  完成验证  构造token
 * @time 2020/8/26 14:09
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  JWTUtil jwtUtil;
    @Autowired
    private MySecurityProperty mySecurityProperty;
    static PathMatcher pathMatcher = new AntPathMatcher();

    public JWTUtil getJwtUtil() {
        return jwtUtil;
    }

    public void setJwtUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public MySecurityProperty getMySecurityProperty() {
        return mySecurityProperty;
    }

    public void setMySecurityProperty(MySecurityProperty mySecurityProperty) {
        this.mySecurityProperty = mySecurityProperty;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {


        SecurityContext context = SecurityContextHolder.getContext();
        Boolean  isNeedJwt=context.getAuthentication()!=null?context.getAuthentication().isAuthenticated():false;//是否已经授权了
        String requestURL = httpServletRequest.getRequestURI();
        if(isNeedJwt){
            log.info("网络请求：{}  已经授权",requestURL);
            filterChain.doFilter(httpServletRequest, httpServletResponse);

            return;

        }

        //如果是不需要权限的url 直接过
        Boolean isNeedAutu=false;
        String[] authUrl = mySecurityProperty.getPermitUrl();
        for(String u:authUrl){
            if(pathMatcher.match(u,requestURL)){
                isNeedAutu=true;
                break;
            }
        }

        if(isNeedAutu){
            log.info("网络请求：{}  为不受限制网址",requestURL);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        Boolean  isCanJwt=false;
        String[] jwtUrl = jwtUtil.getFilter();
        for(String u:jwtUrl){
            if(pathMatcher.match(u,requestURL)){
                isCanJwt=true;
                break;
            }
        }

        if(!isCanJwt){
            log.info("网络请求：{}  不在jwt允许范围内 采用其他方式验证",requestURL);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if(jwtUtil.isEnable()){
            log.info("已开启jwt登录------------> token登录filter处理：");
            String header = httpServletRequest.getHeader(jwtUtil.getHeaderParam());
            logger.info("请求token:"+header);
            if(ValidateUtil.isNotEmpty(header)) {
                Claims claims = jwtUtil.getClaimByToken(header);
                boolean isNoVaild = claims == null || claims.isEmpty() || jwtUtil.isTokenExpired(claims.getExpiration());
                if (isNoVaild) {

                    log.info("token已过期：");
                    filterChain.doFilter(httpServletRequest, httpServletResponse);

                } else {


                    String info = claims.getSubject();
                    logger.info("token携带信息解析:" + info);
                    Map map = JSON.parseObject(info, Map.class);
                    String userName=(String)map.get("userName");
                    List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(JSON.toJSONString(map.get("authList")));
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, "", grantedAuthorities);
                    context.setAuthentication(usernamePasswordAuthenticationToken);

                    filterChain.doFilter(httpServletRequest,httpServletResponse);


                }
            }else {


                filterChain.doFilter(httpServletRequest, httpServletResponse);

            }




        }else {
            log.info("无法使用jwt登录  系统关闭了此种方式");
            filterChain.doFilter(httpServletRequest,httpServletResponse);

        }


    }
}
