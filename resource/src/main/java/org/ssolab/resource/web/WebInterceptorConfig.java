package org.ssolab.resource.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.ssodemo.ssoclient.interceptor.TokenParseInterceptor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/04/13:51
 */

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Value("${sso.validate-location}")
    private String ssoServer;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenParseInterceptor(ssoServer));
    }
}
