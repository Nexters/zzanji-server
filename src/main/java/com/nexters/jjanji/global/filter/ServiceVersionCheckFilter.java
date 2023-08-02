package com.nexters.jjanji.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceVersionCheckFilter implements Filter {
    private static final String HEADER_SERVICE_USER_AGENT = "App-Version";
    private static final double REQUIRED_IOS_APP_VERSION = 0.5;
    private static final double REQUIRED_ANDROID_APP_VERSION = 0.5;
    private static final String DOCUMENT_URI = "/docs/index.html";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String appVersionHeader = request.getHeader(HEADER_SERVICE_USER_AGENT);

        if (!request.getRequestURI().contains(DOCUMENT_URI)) {
            if (appVersionHeader == null || !appVersionHeader.contains("/")) {
                setResponseCodeUpgradeRequired(response);
                return;
            }

            String[] appVersionHeaderInfo = appVersionHeader.split("/");
            if (appVersionHeaderInfo.length != 2) {
                setResponseCodeUpgradeRequired(response);
                return;
            }

            String os = appVersionHeaderInfo[0];
            double appVersion = Double.parseDouble(appVersionHeaderInfo[1]);

            if (!validRequiredVersion(os, appVersion)) {
                setResponseCodeUpgradeRequired(response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private static void setResponseCodeUpgradeRequired(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("지원하지 않는 버전입니다. 업데이트가 필요합니다.");
        response.setStatus(HttpStatus.UPGRADE_REQUIRED.value());
    }

    private boolean validRequiredVersion(String os, double appVersion) {
        if (os.equals("iOS")) {
            return appVersion >= REQUIRED_IOS_APP_VERSION;
        } else if(os.equals("Android")) {
            return appVersion >= REQUIRED_ANDROID_APP_VERSION;
        } else {
            return false;
        }
    }
}