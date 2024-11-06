# SSO-demo
一个实现 sso 单点登录的 demo 方案思路

具体思路: 博客更新中

## 项目结构

- SSO-Server
- SSO-client
- Resource-Server

## 接口设计

- SSO-Server
- SSO-client
- Resource-Server

### Restful-API

##### SSO-Server

登录界面: /sso/login?original_url=[URL编码]

默认登录成功界面：/sso/success

登录接口: /sso/api

| 接口描述       | URI       | 方法 | Request \ Response                                           |
| -------------- | --------- | ---- | ------------------------------------------------------------ |
| 登录接口       | /login    | POST | Request : userId, password, original_url<br />Response : 设置 cookie 和重定向 |
| 登录校验接口   | /validate | GET  | Request : @Nullable acc_token<br />Response :  valid, renewable, updateToken |
| 登出接口       | /logout   | POST | Request : 无<br />Response :  删除 Cookie                    |
| Token 续期接口 | /renewal  | POST | Request：updateToken<br />Response : 更新 cookie             |

##### SSO-client SSO

Spring MVC 拦截器 TokenParseInterceptor

```java
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Value("${sso.validate-location}")
    private String ssoServer;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenParseInterceptor(ssoServer));
    }
}
```

##### Resource-Server

资源界面：/user.html

资源接口：/user/api/info

[GET] /get-info 获取基础信息接口

##### JWT  标准结构示例：

```json
{
  "iss" : "Issuer（发行人）：表示JWT的发行者。",
  "exp" : "Expiration Time（过期时间）：表示JWT的过期时间。",
  "sub" : "Subject（主题）：通常指代JWT的主体，例如用户的ID。",
  "aud" : "Audience（受众）：表示JWT的预期接收者。",
  "nbf" : "Not Before（生效时间）：表示JWT在此时间之前不可用。",
  "iat" : "Issued At（签发时间）：表示JWT的签发时间。",
  "jti" : "JWT ID（JWT的唯一标识符）：JWT的唯一标识符。"
}
```

token 中只需要 sub 和 exp，sub 存储 UserID，exp 存储过期时间。

--- 

前端页面来源:
- kimi.ai 帮我写的
- https://www.bilibili.com/video/BV1w84y1q7MR