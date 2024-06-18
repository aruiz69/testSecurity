package com.example.demo.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.util.UUID;

@Component
public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

    private String headerName = DEFAULT_CSRF_HEADER_NAME;

    private String cookieName = "USER_INFO";

    private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = "X-XSRF-TOKEN";

    private static final String CSRF_TOKEN_ATTR_NAME = DEFAULT_CSRF_TOKEN_ATTR_NAME;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, this.cookieName);
        if (cookie == null) {
            return new DefaultCsrfToken(this.headerName, this.parameterName,
                    createNewToken());
        }
        String cookieValue = cookie.getValue();
        String token = cookieValue.split("\\|")[0];
        if (!StringUtils.hasLength(token)) {
            return new DefaultCsrfToken(this.headerName, this.parameterName,
                    createNewToken());
        }
        return new DefaultCsrfToken(this.headerName, this.parameterName, token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request,
                          HttpServletResponse response) {
        if (token == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(CSRF_TOKEN_ATTR_NAME);
            }
        }
        else {
            HttpSession session = request.getSession();
            session.setAttribute(CSRF_TOKEN_ATTR_NAME, token);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return new CsrfToken() {
            @Override
            public String getHeaderName() {
                return CSRF_TOKEN_ATTR_NAME;
            }

            @Override
            public String getParameterName() {
                return CSRF_TOKEN_ATTR_NAME;
            }

            @Override
            public String getToken() {
                return request.getHeader(CSRF_TOKEN_ATTR_NAME);
            }
        };

    }

    private String createNewToken() {
        return UUID.randomUUID().toString();
    }

}
