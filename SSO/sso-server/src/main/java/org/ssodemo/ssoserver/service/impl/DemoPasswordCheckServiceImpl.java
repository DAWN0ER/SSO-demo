package org.ssodemo.ssoserver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ssodemo.ssoserver.service.PasswordCheckService;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/18:27
 */
@Slf4j
@Service
public class DemoPasswordCheckServiceImpl implements PasswordCheckService {

    private static final HashMap<Long,String> userMapper = new HashMap<>();

    static {
        userMapper.put(1314520L,"pwd");
        userMapper.put(114514L,"homo");
        userMapper.put(1234L,"admin");
    }


    @Override
    public boolean check(Long userId, String password) {
        return userMapper.containsKey(userId) && userMapper.get(userId).equals(password);
    }
}
