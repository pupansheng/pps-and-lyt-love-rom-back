package com.pps.back.frame.pupansheng.core.authority.security.component.common;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户登录时携带的额外信息
 * @author jitwxs
 * @since 2018/5/9 11:15
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 6975601077710753878L;

    //自定义的额外信息
    private final String verifyCode;
    private final  String phone;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        // 自定义的额外信息
        verifyCode = request.getParameter("verifyCode")==null?"":request.getParameter("verifyCode");
        phone=request.getParameter("phone")==null?"":request.getParameter("phone");
    }

    public String getVerifyCode() {
        return this.verifyCode;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; VerifyCode: ").append(this.getVerifyCode()).append("; phone: ")
          .append(this.getPhone());
        return sb.toString();
    }
}
