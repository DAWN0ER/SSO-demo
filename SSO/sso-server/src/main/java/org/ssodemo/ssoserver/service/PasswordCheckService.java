package org.ssodemo.ssoserver.service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/03/18:26
 */
public interface PasswordCheckService {

    boolean check(Long userId,String password);
}
