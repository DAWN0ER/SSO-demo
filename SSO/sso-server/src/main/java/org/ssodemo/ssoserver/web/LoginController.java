package org.ssodemo.ssoserver.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.ssodemo.ssoserver.cache.OnlineStatusCache;
import org.ssodemo.ssoserver.enums.ValidStatusEnum;
import org.ssodemo.ssoserver.pojo.LoginRequestVo;
import org.ssodemo.ssoserver.pojo.OnlineUserDto;
import org.ssodemo.ssoserver.pojo.UserToken;
import org.ssodemo.ssoserver.pojo.ValidateResponseVo;
import org.ssodemo.ssoserver.service.PasswordCheckService;
import org.ssodemo.ssoserver.util.AccessTokenUtil;
import org.ssodemo.ssoserver.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/14:48
 */

@Slf4j
@RestController
@RequestMapping("/sso/api/v1")
public class LoginController {

    private static final String DEFAULT_PAGE = "/sso/success";
    private static final String ACC_TOKEN = "acc_token";
    private static final String DOMAIN = "localhost";
    private static final long RENEW_TIME = 2 * 3600_000;

    @Resource
    @Qualifier("demoPasswordCheckServiceImpl")
    private PasswordCheckService passwordCheckService;

    @Resource
    @Qualifier("localOnlineStatusCache")
    private OnlineStatusCache onlineStatusCache;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestVo loginRequestVo, HttpServletResponse response) throws IOException {
        // 已经登录的不允许再次验证
        UserToken token = AccessTokenUtil.get();
        long tokenUserId = Optional.ofNullable(token).map(UserToken::getUserId).orElse(-1L);
        if (tokenUserId > 0 && onlineStatusCache.isOnline(tokenUserId)) {
            log.info("已登录用户访问登录页面,user:{}", tokenUserId);
            response.sendRedirect(DEFAULT_PAGE);
            return;
        }
        // 正常校验
        Long userId = loginRequestVo.getUserId();
        String password = loginRequestVo.getPassword();
        if (Objects.isNull(userId) || Objects.isNull(password) || "".equals(password)) {
            log.warn("Bad Request, userId:{},password:{}", userId, password);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String redirect = Optional.ofNullable(loginRequestVo.getOriginalUrl()).orElse(DEFAULT_PAGE);
        if (!passwordCheckService.check(userId, password)) {
            log.warn("未找到用户:userId:{}", userId);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String newToken = JwtUtil.generateToken(String.valueOf(userId));
        onlineStatusCache.loginUser(userId, JwtUtil.DEFAULT_EXPIRATION_TIME);
        Cookie cookie = new Cookie(ACC_TOKEN, newToken);
        cookie.setMaxAge(30 * 24 * 3600);
        cookie.setDomain(DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect(redirect);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        UserToken token = AccessTokenUtil.get();
        if (Objects.isNull(token) || Objects.isNull(token.getUserId())) {
            log.warn("未登录用户登出");
            return;
        }
        onlineStatusCache.logoutUser(token.getUserId());
        Cookie cookie = new Cookie(ACC_TOKEN, null);
        cookie.setDomain(DOMAIN);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        log.info("用户:{} 登出下线", token.getUserId());
        response.addCookie(cookie);
    }

    @GetMapping("/validate")
    public ValidateResponseVo validate(@RequestParam(required = false) String accessToken) {
        UserToken userToken = AccessTokenUtil.get();
        if (Objects.nonNull(accessToken)) {
            // 两者都存在时，以参数传递的为准
            userToken = JwtUtil.validate(accessToken);
        }
        ValidateResponseVo responseVo = new ValidateResponseVo();
        if (Objects.isNull(userToken)) {
            log.warn("没有登录信息!");
            responseVo.setValid(ValidStatusEnum.OFFLINE.getCode());
            return responseVo;
        }
        Long userId = userToken.getUserId();
        if (Objects.isNull(userId)) {
            log.warn("没有 UserId 信息!");
            responseVo.setValid(ValidStatusEnum.OFFLINE.getCode());
            return responseVo;
        }
        OnlineUserDto user = onlineStatusCache.getOnlineUser(userId);
        if (Objects.isNull(user)) {
            log.warn("没有保存的登录信息!");
            responseVo.setValid(ValidStatusEnum.OFFLINE.getCode());
            return responseVo;
        }
        log.info("userId:{}, 过期时间:{}", userId, new Date(user.getExpiredTime()));
        responseVo.setValid(ValidStatusEnum.ONLINE.getCode());
        if (user.getExpiredTime() - System.currentTimeMillis() < RENEW_TIME) {
            // TODO 关于令牌更新的还没写
        }
        return responseVo;

    }

    @PostMapping("/renewal")
    public void renewal(
            @RequestParam String updateToken,
            HttpServletRequest request, HttpServletResponse response) {

    }

}
