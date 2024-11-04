package org.ssodemo.ssoclient.interceptor;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ssodemo.ssoclient.domain.ValidateResponseVo;
import org.ssodemo.ssoclient.util.AccessTokenUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/20:21
 */

@Component
public class TokenParseInterceptor implements HandlerInterceptor {

    private final Gson gson = new Gson();

    @Value("${sso.validate-location}")
    private String ssoServer;

    @Value("${sso.cookie-name}")
    private String cookieName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String value = null;
        if (Objects.isNull(request.getCookies())) {
            AccessTokenUtil.set(null);
            return true;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                value = cookie.getValue();
            }
        }
        if (Objects.isNull(value)) {
            AccessTokenUtil.set(null);
            return true;
        }
        try {
            HttpClient client = HttpClients.createMinimal();
            URI uri = new URIBuilder(ssoServer).addParameter("accessToken", value).build();
            HttpResponse validResp = client.execute(new HttpGet(uri));
            if (Objects.isNull(validResp.getEntity())){
                AccessTokenUtil.set(null);
                return true;
            }
            String json = EntityUtils.toString(validResp.getEntity());
            ValidateResponseVo responseVo = gson.fromJson(json, ValidateResponseVo.class);
            if(Objects.nonNull(responseVo) && responseVo.isValid()){
                // TODO 这是验证有效的逻辑

            }

        } catch (Exception e) {

        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        AccessTokenUtil.remove();
    }

}
