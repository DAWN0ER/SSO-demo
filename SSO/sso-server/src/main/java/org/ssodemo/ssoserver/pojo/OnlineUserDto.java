package org.ssodemo.ssoserver.pojo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/06/21:50
 */

@Data
public class OnlineUserDto {
    private long userId;
    private long expiredTime;
}
