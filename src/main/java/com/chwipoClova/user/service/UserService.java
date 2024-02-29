package com.chwipoClova.user.service;

import com.chwipoClova.common.exception.CommonException;
import com.chwipoClova.common.exception.ExceptionCode;
import com.chwipoClova.common.repository.LogRepository;
import com.chwipoClova.common.response.CommonResponse;
import com.chwipoClova.common.response.MessageCode;
import com.chwipoClova.common.utils.JwtUtil;
import com.chwipoClova.token.dto.TokenDto;
import com.chwipoClova.token.entity.Token;
import com.chwipoClova.token.service.TokenService;
import com.chwipoClova.user.dto.KakaoToken;
import com.chwipoClova.user.dto.KakaoUserInfo;
import com.chwipoClova.user.entity.User;
import com.chwipoClova.user.repository.UserRepository;
import com.chwipoClova.user.request.UserLogoutReq;
import com.chwipoClova.user.response.UserInfoRes;
import com.chwipoClova.user.response.UserLoginRes;
import com.chwipoClova.user.response.UserSnsUrlRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;

    private final JwtUtil jwtUtil;

    @Value("${kakao.url.auth}")
    private String kakaoAuthUrl;

    @Value("${kakao.url.token}")
    private String tokenUrl;

    @Value("${kakao.url.api}")
    private String apiUrl;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.client_secret}")
    private String clientSecret;

    @Value("${kakao.grant_type}")
    private String grantType;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.redirect_local_uri}")
    private String redirectLocalUri;

    private final LogRepository logRepository;

    public UserSnsUrlRes getKakaoUrl() {
        String kakaoUrl = kakaoAuthUrl + "?response_type=code" + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri;
        UserSnsUrlRes userSnsUrlRes = UserSnsUrlRes.builder()
                .url(kakaoUrl)
                .build();
        return userSnsUrlRes;
    }

    @Transactional
    public CommonResponse kakaoLogin(String code, HttpServletResponse response) {
        KakaoToken kakaoToken = requestAccessToken(code);
        KakaoUserInfo kakaoUserInfo = requestOauthInfo(kakaoToken);

        long snsId = kakaoUserInfo.getId();
        String email = kakaoUserInfo.getEmail();
        String nickname = kakaoUserInfo.getNickname();
        Integer snsType = kakaoUserInfo.getOAuthProvider().getCode();
        String thumbnailImageUrl = kakaoUserInfo.getThumbnailImageUrl();
        String profileImageUrl = kakaoUserInfo.getProfileImageUrl();

        Optional<User> userInfo = userRepository.findBySnsTypeAndSnsId(snsType, snsId);

        // 유저 정보가 있다면 업데이트 없으면 등록
        if (userInfo.isPresent()) {
            User userInfoRst = userInfo.get();

            Long userId = userInfoRst.getUserId();

            String strUserId = String.valueOf(userId);

            // 로그인 할때마다 토큰 새로 발급(갱신)
            TokenDto tokenDto = jwtUtil.createAllToken(strUserId);
//            Token newToken = new Token(tokenDto.getRefreshToken(),  strUserId);
//            tokenService.save(newToken);

            // Refresh 토큰 있는지 확인
           // Token refreshToken = tokenService.findById(strUserId);

            // 있다면 새토큰 발급후 업데이트
            // 없다면 새로 만들고 디비 저장
//            if(refreshToken != null) {
//                tokenService.save(refreshToken);
//            }else {
//                Token newToken = new Token(tokenDto.getRefreshToken(),  strUserId);
//                tokenService.save(newToken);
//            }

            // response 헤더에 Access Token / Refresh Token 넣음
            jwtUtil.setResonseJwtToken(response, tokenDto);

            UserLoginRes userLoginRes = UserLoginRes.builder()
                    .snsId(userInfoRst.getSnsId())
                    .userId(userId)
                    .email(userInfoRst.getEmail())
                    .name(userInfoRst.getName())
                    .snsType(userInfoRst.getSnsType())
                    .thumbnailImage(userInfoRst.getThumbnailImage())
                    .profileImage(userInfoRst.getProfileImage())
                    .regDate(userInfoRst.getRegDate())
                    .modifyDate(userInfoRst.getModifyDate())
                    .build();
            log.info("기존유저 {}, {}",userLoginRes.getUserId(), userLoginRes.getName());
            // API 로그 적재
            logRepository.loginLogSave(userLoginRes.getUserId(), "기존유저 " + userLoginRes.getUserId() + "," + userLoginRes.getName());
            return new CommonResponse<>(String.valueOf(HttpStatus.OK.value()), userLoginRes, HttpStatus.OK.getReasonPhrase());
        } else {
            User user = User.builder()
                    .snsId(snsId)
                    .email(email)
                    .name(nickname)
                    .snsType(snsType)
                    .thumbnailImage(thumbnailImageUrl)
                    .profileImage(profileImageUrl)
                    .regDate(new Date())
                    .build();
            User userResult = userRepository.save(user);
            log.info("신규유저 {}, {}",userResult.getUserId(), userResult.getName());
            // API 로그 적재
            logRepository.loginLogSave(userResult.getUserId(), "신규유저 " + userResult.getUserId() + userResult.getName());
            return new CommonResponse<>(MessageCode.NEW_USER.getCode(), null, MessageCode.NEW_USER.getMessage());
        }

    }

    public KakaoToken requestAccessToken(String code) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();;
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        KakaoToken response = restTemplate.postForObject(tokenUrl, request, KakaoToken.class);

        // TODO 토큰 정보를 가져오지 못하면 예외발생 처리 추가
        assert response != null;
        return response;
    }

    public KakaoUserInfo requestOauthInfo(KakaoToken kakaoToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + kakaoToken.getAccessToken());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();;
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
        KakaoUserInfo response = restTemplate.postForObject(apiUrl, request, KakaoUserInfo.class);

        // TODO 유저 정보를 가져오지 못하면 예외발생 처리 추가
        assert response != null;
        return response;
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    public UserInfoRes selectUserInfo(String email) {
        Optional<User> usersInfo = userRepository.findByEmailAndSnsType(email, 1);
        if (!usersInfo.isPresent()) {
            throw new CommonException(ExceptionCode.USER_NULL.getMessage(), ExceptionCode.USER_NULL.getCode());
        }

        User user = usersInfo.get();

        return UserInfoRes.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .thumbnailImage(user.getThumbnailImage())
                .profileImage(user.getProfileImage())
                .regDate(user.getRegDate())
                .modifyDate(user.getModifyDate())
                .build();
    }

    public CommonResponse logout(HttpServletRequest request, HttpServletResponse response, UserLogoutReq userLogoutReq) {
//        Long userId = userLogoutReq.getUserId();
//        String strUserId = String.valueOf(userId);
//        tokenService.deleteById(strUserId);

        // 로그아웃은 무조건 성공
        try {
            jwtUtil.deleteAllToken(request, response);
        } catch (Exception e) {
            log.error("로그아웃 에러 발생 {}", e.getMessage());
        }
        return new CommonResponse<>(MessageCode.OK.getCode(), null, MessageCode.OK.getMessage());
    }

    public UserInfoRes selectUserInfoForUserId(Long userId) {
        Optional<User> usersInfo = userRepository.findById(userId);
        if (!usersInfo.isPresent()) {
            throw new CommonException(ExceptionCode.USER_NULL.getMessage(), ExceptionCode.USER_NULL.getCode());
        }

        User user = usersInfo.get();

        return UserInfoRes.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .thumbnailImage(user.getThumbnailImage())
                .profileImage(user.getProfileImage())
                .regDate(user.getRegDate())
                .modifyDate(user.getModifyDate())
                .build();
    }

    public UserSnsUrlRes getKakaoLocalUrl() {
        String kakaoUrl = kakaoAuthUrl + "?response_type=code" + "&client_id=" + clientId
                + "&redirect_uri=" + redirectLocalUri;
        UserSnsUrlRes userSnsUrlRes = UserSnsUrlRes.builder()
                .url(kakaoUrl)
                .build();
        return userSnsUrlRes;
    }

    public CommonResponse kakaoDevLogin(String code, HttpServletResponse response) {
        KakaoToken kakaoToken = requestDevAccessToken(code);
        KakaoUserInfo kakaoUserInfo = requestOauthInfo(kakaoToken);

        long snsId = kakaoUserInfo.getId();
        String email = kakaoUserInfo.getEmail();
        String nickname = kakaoUserInfo.getNickname();
        Integer snsType = kakaoUserInfo.getOAuthProvider().getCode();
        String thumbnailImageUrl = kakaoUserInfo.getThumbnailImageUrl();
        String profileImageUrl = kakaoUserInfo.getProfileImageUrl();

        Optional<User> userInfo = userRepository.findBySnsTypeAndSnsId(snsType, snsId);

        // 유저 정보가 있다면 업데이트 없으면 등록
        if (userInfo.isPresent()) {
            User userInfoRst = userInfo.get();

            Long userId = userInfoRst.getUserId();
            String strUserId = String.valueOf(userId);
            TokenDto tokenDto = jwtUtil.createAllToken(strUserId);

            // Refresh토큰 있는지 확인
//            Token refreshToken = tokenService.findById(strUserId);

            // 있다면 새토큰 발급후 업데이트
            // 없다면 새로 만들고 디비 저장
//            if(refreshToken != null) {
//                tokenService.save(refreshToken.updateToken(tokenDto.getRefreshToken()));
//            }else {
//                Token newToken = new Token(tokenDto.getRefreshToken(), strUserId);
//                tokenService.save(newToken);
//            }

            // response 헤더에 Access Token / Refresh Token 넣음
            jwtUtil.setResonseJwtToken(response, tokenDto);

            UserLoginRes userLoginRes = UserLoginRes.builder()
                    .snsId(userInfoRst.getSnsId())
                    .userId(userId)
                    .email(userInfoRst.getEmail())
                    .name(userInfoRst.getName())
                    .snsType(userInfoRst.getSnsType())
                    .thumbnailImage(userInfoRst.getThumbnailImage())
                    .profileImage(userInfoRst.getProfileImage())
                    .regDate(userInfoRst.getRegDate())
                    .modifyDate(userInfoRst.getModifyDate())
                    .build();
            log.info("기존유저 {}, {}",userLoginRes.getUserId(), userLoginRes.getName());
            logRepository.loginLogSave(userLoginRes.getUserId(), "기존유저 " + userLoginRes.getUserId() + "," + userLoginRes.getName());
            return new CommonResponse<>(String.valueOf(HttpStatus.OK.value()), userLoginRes, HttpStatus.OK.getReasonPhrase());
        } else {
            User user = User.builder()
                    .snsId(snsId)
                    .email(email)
                    .name(nickname)
                    .snsType(snsType)
                    .thumbnailImage(thumbnailImageUrl)
                    .profileImage(profileImageUrl)
                    .regDate(new Date())
                    .build();
            User userResult = userRepository.save(user);
            log.info("신규유저 {}, {}",userResult.getUserId(), userResult.getName());
            logRepository.loginLogSave(userResult.getUserId(), "신규유저 " + userResult.getUserId() + userResult.getName());
            return new CommonResponse<>(MessageCode.NEW_USER.getCode(), null, MessageCode.NEW_USER.getMessage());
        }
    }

    private KakaoToken requestDevAccessToken(String code) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();;
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectLocalUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        KakaoToken response = restTemplate.postForObject(tokenUrl, request, KakaoToken.class);

        // TODO 토큰 정보를 가져오지 못하면 예외발생 처리 추가
        assert response != null;
        return response;
    }
}
