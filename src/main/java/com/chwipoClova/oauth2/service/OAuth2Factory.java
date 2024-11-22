package com.chwipoClova.oauth2.service;

import com.chwipoClova.oauth2.dto.GoogleOAuth2UserInfo;
import com.chwipoClova.oauth2.dto.UserInfo;
import com.chwipoClova.oauth2.enums.UserLoginType;
import com.chwipoClova.oauth2.exception.OAuth2AuthenticationProcessingException;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class OAuth2Factory {

    public static UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String, Object> attributes) {
        if (UserLoginType.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("OAuth2Factory Exception : " + registrationId);
        }
    }
}
