package org.ssodemo.ssoclient.domain;

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

    private boolean valid;
    private boolean renewable;
    private String  updateToken;

}
