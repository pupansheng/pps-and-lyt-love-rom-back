package com.pps.back.frame.pupansheng.core.authority.security.property;



import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT类
 *
 * @author
 * @date 2018-05-18
 */
@Component
@ConfigurationProperties(prefix = "mysecurity.jwt")
@Slf4j
public class JWTUtil {

    private  boolean enable;


    private  String  headerParam="authorization";
    /**
     * 加密秘钥
     */
    private String secret;
    /**
     * 有效时间
     */
    private long expire;

    private  String jwtLoginUrl;

    private  String [] filter;

   private  boolean isHasAdd=false;
    public void  addPrefix(String prefix){
        if(!isHasAdd) {

            if (filter != null && filter.length > 0) {
                for (String url : filter) {
                    url = prefix + url;
                }
            }
            if (ValidateUtil.isNotEmpty(jwtLoginUrl)) {
                jwtLoginUrl = prefix + jwtLoginUrl;
            }

            isHasAdd=true;
        }

    }

    public String[] getFilter() {
        return filter;
    }

    public void setFilter(String[] filter) {
        this.filter = filter;
    }

    public String getJwtLoginUrl() {
        return jwtLoginUrl;
    }

    public void setJwtLoginUrl(String jwtLoginUrl) {
        this.jwtLoginUrl = jwtLoginUrl;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getHeaderParam() {
        return headerParam;
    }

    public void setHeaderParam(String headerParam) {
        this.headerParam = headerParam;
    }

    /**
     * 获取:加密秘钥
     */

    public String getSecret() {
        return secret;
    }

    /**
     * 设置:加密秘钥
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 获取:有效期(s)
     * */
    public long getExpire() {
        return expire;
    }
    /**
     * 设置:有效期(s)
     * */
    public void setExpire(long expire) {
        this.expire = expire;
    }


    /**
     * 生成Token签名
     * @param info
     * @return 签名字符串
     * @author geYang
     * @date 2018-05-18 16:35
     */
    public String generateToken(String info) {
        log.info("header=" + headerParam + ", expire=" + getExpire() + ", secret=" + getSecret());
        Date nowDate = new Date();
        // 过期时间

        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(info))
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, getSecret()).compact();
        // 注意: JDK版本高于1.8, 缺少 javax.xml.bind.DatatypeConverter jar包,编译出错
    }

    /**
     * 获取签名信息
     * @param token
     * @author geYang
     * @date 2018-05-18 16:47
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.info("validate is token error ", e);
            return null;
        }
    }

    /**
     * 判断Token是否过期
     * @param expiration
     * @return true 过期
     * @author geYang
     * @date 2018-05-18 16:41
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

}