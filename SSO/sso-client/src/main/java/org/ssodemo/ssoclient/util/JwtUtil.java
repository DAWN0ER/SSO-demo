package org.ssodemo.ssoclient.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/04/15:46
 */
@Slf4j
public class JwtUtil {

    private static final Gson gson = new Gson();

    public static String getPayLoadJson(String jwt) {
        if (Objects.isNull(jwt)) {
            return null;
        }
        String[] parts = jwt.split("\\.");
        if (parts.length < 3) {
            return null;
        }
        byte[] decode = Base64.getDecoder().decode(parts[1]);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public static JwsPayload getJwsPayload(String jwt) {
        String json = getPayLoadJson(jwt);
        try {
            return gson.fromJson(json, JwsPayload.class);
        } catch (JsonSyntaxException e) {
            log.error("解析 payload 失败:{}",json);
            return null;
        }
    }

    public static class JwsPayload {
        long sub;
        long exp;

        public long getSub() {
            return sub;
        }

        public void setSub(long sub) {
            this.sub = sub;
        }

        public long getExp() {
            return exp;
        }

        public void setExp(long exp) {
            this.exp = exp;
        }


    }

}
