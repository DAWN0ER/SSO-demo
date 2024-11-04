package org.ssolab.resource.service;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.ssolab.resource.pojo.UserVo;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/01/13:52
 */

@Service
public class UserService implements InitializingBean {

    private final HashMap<Long, UserVo> store = new HashMap<>();


    public UserVo getUser(Long userId) {
        return store.get(userId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        store.put(1234L, new UserVo(1234L, "AAAdmin", "管理员", 1314, 520, 99999, "/img/u1.jpg"));
        store.put(114514L, new UserVo(114514L, "Homo Master", "念念不忘必有回想", 5201314, 1, 9999, "/img/u2.jpg"));
        store.put(1314520L, new UserVo(1314520L, "大爱仙尊", "岂不闻天无绝人之路", 1155664264, 1, 3346584, "/img/u3.jpg"));
    }
}
