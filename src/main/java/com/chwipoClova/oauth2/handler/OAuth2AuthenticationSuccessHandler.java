package com.chwipoClova.oauth2.handler;

import com.chwipoClova.common.utils.JwtUtil;
import com.chwipoClova.oauth2.dto.OAuth2UserInfoRecord;
import com.chwipoClova.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${home.url}")
    private String homeUrl;

    @Value("${home.error}")
    private String error;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2UserInfoRecord userInfoRecord = getOAuth2UserPrincipal(authentication);

        String url = "";
        if (userInfoRecord == null || userInfoRecord.userInfo() == null) {
            url = UriComponentsBuilder.fromUriString(homeUrl+error)
                    .build().toUriString();
        } else {
            url = UriComponentsBuilder.fromUriString(homeUrl).build().toUriString();
            userService.loginGoogle(userInfoRecord.userInfo(), response);
        }
        //clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private OAuth2UserInfoRecord getOAuth2UserPrincipal(Authentication authentication) {
        Object userInfo = authentication.getPrincipal();

        if (userInfo instanceof OAuth2UserInfoRecord) {
            return (OAuth2UserInfoRecord) userInfo;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        jwtUtil.deleteAllToken(request, response);
    }
}
