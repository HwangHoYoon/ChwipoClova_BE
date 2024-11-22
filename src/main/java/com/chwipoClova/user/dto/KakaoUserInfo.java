package com.chwipoClova.user.dto;

import com.chwipoClova.oauth2.dto.UserInfo;
import com.chwipoClova.oauth2.enums.UserLoginType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo implements UserInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("connected_at")
    private Date connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;

        private String email;

    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;

        private String thumbnail_image_url;

        private String profile_image_url;

        private boolean is_default_image;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getName() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getFirstName() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getLastName() {
        return kakaoAccount.profile.nickname;
    }

    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getThumbnailImageUrl() {
        return kakaoAccount.profile.thumbnail_image_url;
    }

    public String getProfileImageUrl() {
        return kakaoAccount.profile.profile_image_url;
    }

    public boolean isDefaultImage() {
        return kakaoAccount.profile.is_default_image;
    }

    public UserLoginType getOAuthProvider() {
        return UserLoginType.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return "";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
}
