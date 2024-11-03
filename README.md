# SSO-demo
一个实现 sso 单点登录的 demo 方案思路

## 项目结构

- SSO-Server
- SSO-client
- Resource-Server

### SSO-Server 单点登录授权服务器：

登录界面: /sso/login?original_url=[URL编码]

登录接口: /sso/api

- [POST] /login 登录接口
    - Request：userId，password，original_url
    - Response：设置 cookie 和重定向

- [GET] /validate 登录校验接口，提供 Https 校验方式，参数或者 Cookie 校验
    - Request：@Nullable acc_token
    - Response：登录状态，ONLINE，OFFLINE，BLACKLISTED
- [POST] /logout 登出键，只允许 Cookie 登出
    - Request：无
- [POST] /renewal JWT 续期接口
    - Request：advice_code

### SSO-client SSO 客户端工具包：

SpringMvc 拦截器

### Resource-Server 资源服务器：

资源界面：/user/info.html

资源接口：/user/api/

- [GET] /get-info 获取基础信息接口

## 要点思考

##### SSO 的高性能和高可用：

在访问量很少的时候，可以直接考虑单点负载，登录登出状态直接保存在缓存里就行，服务下线则用户下线

在访问量增加的时候，SSO 的性能瓶颈会在 JWT 解析和用户登录（涉及到数据库瓶颈）这两个功能上面，所以采用水平扩展然后 Redis 作为一致性缓存的方案即可。

##### JWT 的续期：

提供续期接口，SSO 客户端每次验权的时候，SSO 服务可以返回一个 JWT 续期的 Advice（随机码，有效时常5分钟），客户端可以自己选择是否在响应头里设置这个 Advice，如果设置好的话 js 拿到这个 advice 去请求续期接口。

##### CSRF 防护:

一般来说，可以使用这个方案：

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(); // 启用CSRF保护
    }
}
```

但是深入原理了解得知，Spring Security中的CSRF（跨站请求伪造）保护原理主要基于同步器令牌模式（Synchronizer Token Pattern），而这个随机字段是添加在 Session 里面的，对于单节点来说，这个当然没有上什么问题，但是，对于集群来说，问题就大了。

对于后端集群，我们通常采用 Nginx 作为反向代理，Nginx 的默认反向代理模式是轮流代理，也就是会导致前后两次访问路由不到同一个服务器上，Session 就没啥用了，这个时候还是得用 Redis 代替 Session 的功能。