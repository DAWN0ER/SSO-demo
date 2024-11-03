package org.ssodemo.ssoserver.pojo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * Description:
 * author Dawn Yang
 * since 2024/11/03/15:16
 */

@Data
public class ValidateResponseVo {

    private Integer status;
    private Boolean renewable = false;
    private String  updateToken;

}
