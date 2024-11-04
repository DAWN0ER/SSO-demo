package org.ssolab.resource.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssodemo.ssoclient.domain.UserToken;
import org.ssodemo.ssoclient.util.AccessTokenUtil;
import org.ssolab.resource.pojo.UserVo;
import org.ssolab.resource.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/01/13:47
 */

@Slf4j
@RestController
@RequestMapping("/user/api")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/info")
    public UserVo getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserToken userToken = AccessTokenUtil.get();
        if (Objects.isNull(userToken)) {
            log.warn("用户不存在");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        log.info("用户请求资料:{}",userToken.getUserId());
        return userService.getUser(userToken.getUserId());
    }

}
