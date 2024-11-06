package org.ssodemo.ssoserver.enums;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2024/11/06/21:45
 */
public enum ValidStatusEnum {
    ONLINE(1),
    OFFLINE(0),
    BLACKLISTED(-1),
    ;

    private final int code;

    ValidStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
