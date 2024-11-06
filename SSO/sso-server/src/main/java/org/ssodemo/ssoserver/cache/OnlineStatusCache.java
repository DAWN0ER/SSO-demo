package org.ssodemo.ssoserver.cache;

import org.ssodemo.ssoserver.pojo.OnlineUserDto;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/15:56
 */
public interface OnlineStatusCache {

    boolean isOnline(long userId);
    void loginUser(long userId, long keepAlive);
    void logoutUser(long userId);
    OnlineUserDto getOnlineUser(long userId);

}
