package org.ssodemo.ssoserver.web.interaptor;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ssodemo.ssoserver.util.AccessTokenUtil;
import org.ssodemo.ssoserver.util.JwtUtil;
import org.ssodemo.ssoserver.util.UserToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/20:21
 */
public class TokenParseInterceptor implements HandlerInterceptor {

    private static final String ACC_TOKEN = "acc_token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserToken token = null;
        for (Cookie cookie : request.getCookies()) {
            if (ACC_TOKEN.equals(cookie.getName())) {
                String value = cookie.getValue();
                token = JwtUtil.validate(value);
            }
        }
        AccessTokenUtil.set(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                         @Nullable Exception ex) throws Exception {
        AccessTokenUtil.remove();
    }

}
