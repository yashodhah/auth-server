Will secure any request with

[org.springframework.security.web.session.DisableEncodeUrlFilter@5f85ee0,
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@5e9e69a1,
org.springframework.security.web.context.SecurityContextPersistenceFilter@219fbf1b,
org.springframework.security.web.header.HeaderWriterFilter@35825500,
org.springframework.security.web.csrf.CsrfFilter@66d7c113,
org.springframework.security.web.authentication.logout.LogoutFilter@1bc3fe42,
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@14c2cae9, --> Tries to find a username/password request parameter/POST body
org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@1a1a32bf, --> Default Loin page
org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@2d470f4c,  --> Default logout page
org.springframework.security.web.authentication.www.BasicAuthenticationFilter@3ca8d1df, --> try to find Basic Auth HTTP headers
org.springframework.security.web.savedrequest.RequestCacheAwareFilter@478ce8a7,
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@7c3791c9,
org.springframework.security.web.authentication.AnonymousAuthenticationFilter@44fc6b22,
org.springframework.security.web.session.SessionManagementFilter@772c299,
org.springframework.security.web.access.ExceptionTranslationFilter@4effc036,
org.springframework.security.web.access.intercept.FilterSecurityInterceptor@f2e93a6 --> Authroization
]

database schemas used by the framework
https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/appendix-schema.html



