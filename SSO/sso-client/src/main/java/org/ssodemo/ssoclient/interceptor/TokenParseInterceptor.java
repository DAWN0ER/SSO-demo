package org.ssodemo.ssoclient.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ssodemo.ssoclient.domain.UserToken;
import org.ssodemo.ssoclient.util.AccessTokenUtil;
import org.ssodemo.ssoclient.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
        if(Objects.isNull(request.getCookies())){
            AccessTokenUtil.set(null);
            return true;
        }
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
