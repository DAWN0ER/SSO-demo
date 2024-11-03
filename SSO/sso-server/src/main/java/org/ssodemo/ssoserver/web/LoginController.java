package org.ssodemo.ssoserver.web;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.ssodemo.ssoserver.pojo.LoginRequestVo;
import org.ssodemo.ssoserver.pojo.ValidateResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * Description:
 * @author  Dawn Yang
 * @since 2024/11/03/14:48
 */

@Slf4j
@RestController
@RequestMapping("/sso/api/v1")
public class LoginController {

    @PostMapping("/login")
    public void login(
            @RequestBody LoginRequestVo loginRequestVo,
            HttpServletRequest request,HttpServletResponse response) {
        log.info(new Gson().toJson(loginRequestVo));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response) {

    }

    @GetMapping("/validate")
    public ValidateResponseVo validate(
            @RequestParam(required = false) String accessToken,
            HttpServletRequest request,HttpServletResponse response) {
        return new ValidateResponseVo();
    }

    @PostMapping("/renewal")
    public void renewal(
            @RequestParam String updateToken,
            HttpServletRequest request,HttpServletResponse response) {

    }

}
