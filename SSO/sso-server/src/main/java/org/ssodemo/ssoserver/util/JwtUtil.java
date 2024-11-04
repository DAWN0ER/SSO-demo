package org.ssodemo.ssoserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.ssodemo.ssoserver.pojo.UserToken;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/15:44
 */

@Slf4j
public class JwtUtil {

    public static final long DEFAULT_EXPIRATION_TIME = 7 * 24 * 3600_000; // 7 days

    public static String generateToken(String userId) {
        // 从配置中心获取 key
        String jwtSecretKey = ConfigUtil.getJwtSecretKey();

        return Jwts.builder()
                .setSubject(userId) // 唯一有用的 PayLoad 实际上就是 UserId
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public static UserToken validate(String token) {
        // 从配置中心获取 key
        String jwtSecretKey = ConfigUtil.getJwtSecretKey();

        JwtParser parser = Jwts.parser().setSigningKey(jwtSecretKey);
        try {
            Claims claims = parser.parseClaimsJws(token).getBody();
            UserToken userToken = new UserToken();
            userToken.setUserId(Long.parseLong(claims.getSubject()));
            userToken.setExpiration(claims.getExpiration());
            return userToken;
        } catch (Exception e) {
            log.error("Jwt 校验失败 :{}",token,e);
            return null;
        }
    }


}

