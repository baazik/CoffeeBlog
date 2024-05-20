package cz.hocuspocus.coffeeblog.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response);
        if (isUrlValid(targetUrl)) {
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/articles");
        }
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String prevPage = (String) request.getSession().getAttribute("prevPage");
        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
            return prevPage;
        }
        return "/articles";
    }

    private boolean isUrlValid(String url) {
        if (url.contains("/account/reset-password")) {
            return false;
        }
        return true;
    }
}