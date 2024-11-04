package org.ssolab.resource.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * Description:
 * @author  Dawn Yang
 * @since  2024/11/01/13:48
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private Long id;
    private String name;
    private String description;
    private Integer likes;
    private Integer followers;
    private Integer following;
    private String avatar;

}
