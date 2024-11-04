package org.ssodemo.ssoclient.util;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/04/15:46
 */
public class JwtUtil {

    public static String getPayLoad(String jwt){
        String[] parts = jwt.split("\\.");
        // TODO 没写完
        return null;
    }


    private static class JwsPayload{
        long sub;
        long exp;
    }

}
