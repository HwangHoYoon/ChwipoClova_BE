package com.chwipoClova.oauth2.service;

import com.chwipoClova.oauth2.dto.OAuth2UserInfoRecord;
import com.chwipoClova.oauth2.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        String token = oAuth2UserRequest.getAccessToken().getTokenValue();

        UserInfo oAuth2UserInfo = OAuth2Factory.getOAuth2UserInfo(registrationId, token, oAuth2User.getAttributes());

        return new OAuth2UserInfoRecord(oAuth2UserInfo);
    }
}