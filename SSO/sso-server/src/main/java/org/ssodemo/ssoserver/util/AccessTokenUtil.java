package org.ssodemo.ssoserver.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.ssodemo.ssoserver.pojo.UserToken;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/20:23
 */
public class AccessTokenUtil {

    private static final TransmittableThreadLocal<UserToken> ACC_TOKEN_TTL = new TransmittableThreadLocal<>(true);

    public static void set(UserToken token){
        ACC_TOKEN_TTL.set(token);
    }

    public static UserToken get(){
        return ACC_TOKEN_TTL.get();
    }

    public static void remove(){
        ACC_TOKEN_TTL.remove();
    }

}
