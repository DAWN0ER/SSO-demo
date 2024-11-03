package org.ssodemo.ssoserver.utils;

import org.junit.jupiter.api.Test;
import org.ssodemo.ssoserver.util.JwtUtil;
import org.ssodemo.ssoserver.util.UserToken;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/16:28
 */
public class JwtTest {

    @Test
    public void test(){
        String token = JwtUtil.generateToken("445sdr43fte");
        System.out.println("token = " + token);
        UserToken claims = JwtUtil.validate(token);
        System.out.println("claims = " + claims);
    }
}
