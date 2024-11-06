package org.ssodemo.ssoserver.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ssodemo.ssoserver.cache.OnlineStatusCache;
import org.ssodemo.ssoserver.pojo.OnlineUserDto;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/16:00
 */

@Slf4j
@Service
public class LocalOnlineStatusCache implements OnlineStatusCache {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(
            1,
            r -> {
                Thread thread = new Thread(r, "l-cache-clean");
                thread.setDaemon(true);
                return thread;
            }
    );

    private static final ConcurrentHashMap<Long, Long> ONLINE_USER_CACHE = new ConcurrentHashMap<>(1024);

    static {
        // 注册一个定时任务，服务启动1000毫秒后，每隔500毫秒执行一次
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(
                LocalOnlineStatusCache::cleanExpiredUser,
                1000L,
                500L,
                TimeUnit.MILLISECONDS
        );
    }

    private static void cleanExpiredUser() {
        try {
            ONLINE_USER_CACHE.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
        } catch (Exception e) {
            log.error("定时清除任务异常:",e);
        }
    }

    @Override
    public boolean isOnline(long userId) {
        if (ONLINE_USER_CACHE.containsKey(userId)) {
            Long expiredTime = ONLINE_USER_CACHE.getOrDefault(userId, -1L);
            return expiredTime > System.currentTimeMillis();
        }
        return false;
    }

    @Override
    public void loginUser(long userId, long keepAlive) {
        if(userId<=0 || keepAlive <= 0){
            return;
        }
        long expiredTime = System.currentTimeMillis() + keepAlive;
        ONLINE_USER_CACHE.put(userId, expiredTime);
    }


    @Override
    public void logoutUser(long userId) {
        ONLINE_USER_CACHE.remove(userId);
    }

    @Override
    public OnlineUserDto getOnlineUser(long userId) {
        if(!isOnline(userId)){
           return null;
        }
        Long expiredTime = ONLINE_USER_CACHE.get(userId);
        OnlineUserDto userDto = new OnlineUserDto();
        userDto.setUserId(userId);
        userDto.setExpiredTime(expiredTime);
        return userDto;
    }
}
