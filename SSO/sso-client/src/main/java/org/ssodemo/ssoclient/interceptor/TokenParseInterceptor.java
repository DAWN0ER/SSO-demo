package org.ssodemo.ssoclient.interceptor;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ssodemo.ssoclient.domain.UserToken;
import org.ssodemo.ssoclient.domain.ValidateResponseVo;
import org.ssodemo.ssoclient.util.AccessTokenUtil;
import org.ssodemo.ssoclient.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Date;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/20:21
 */

@Slf4j
public class TokenParseInterceptor implements HandlerInterceptor {

    private final Gson gson = new Gson();

    private final String cookieName = "acc_token";

    private final String ssoServerUri;

    public TokenParseInterceptor(String ssoServerUri) {
        this.ssoServerUri = ssoServerUri;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwtStr = null;
        if (Objects.isNull(request.getCookies())) {
            AccessTokenUtil.set(null);
            return true;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                jwtStr = cookie.getValue();
            }
        }
        if (Objects.isNull(jwtStr)) {
            log.info("请求无登录状态信息:{}", request.getRequestURI());
            AccessTokenUtil.set(null);
            return true;
        }
        try {
            HttpClient client = HttpClients.createMinimal();
            URI uri = new URIBuilder(ssoServerUri).addParameter("accessToken", jwtStr).build();
            log.info("发起校验请求:{}",uri.toString());
            HttpResponse validResp = client.execute(new HttpGet(uri));
            if (Objects.isNull(validResp.getEntity())) {
                AccessTokenUtil.set(null);
                return true;
            }
            String json = EntityUtils.toString(validResp.getEntity());
            ValidateResponseVo responseVo = gson.fromJson(json, ValidateResponseVo.class);
            if (Objects.isNull(responseVo) || !responseVo.isValid()) {
                log.error(" jwt 登录失效或已过期:{}", validResp);
                AccessTokenUtil.set(null);
                return true;
            }
            JwtUtil.JwsPayload jwsPayload = JwtUtil.getJwsPayload(jwtStr);
            if (Objects.isNull(jwsPayload)) {
                log.error("本地解析 jwt 失败:{}", jwtStr);
                AccessTokenUtil.set(null);
                return true;
            }
            UserToken userToken = new UserToken();
            userToken.setUserId(jwsPayload.getSub());
            userToken.setExpiration(new Date(jwsPayload.getExp()));
            AccessTokenUtil.set(userToken);
            return true;
        } catch (Exception e) {
            log.error("获取登录状态异常:", e);
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        AccessTokenUtil.remove();
    }

}
