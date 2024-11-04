package org.ssodemo.ssoserver.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/17:05
 */

@Data
public class UserToken {

    private Long userId;
    private Date expiration;

}
