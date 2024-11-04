package org.ssodemo.ssoserver.pojo;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * Created with IntelliJ IDEA.
 *
 * Description:
 * author: Dawn Yang
 * since: 2024/11/03/14:51
 */

@Data
public class LoginRequestVo {

    private Long userId;
    private String password;

    @Nullable
    private String originalUrl;

}
