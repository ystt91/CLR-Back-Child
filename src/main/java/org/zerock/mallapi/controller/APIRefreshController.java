package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.mallapi.util.CustomJWTException;
import org.zerock.mallapi.util.JWTUtil;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String,Object> refresh(
            @RequestHeader("Authorization") String authHeader, // authHeader는 accessToken
            String refreshToken
    ) {

        if(refreshToken == null){
            throw new CustomJWTException("NULL_REFRESH");
        }

        if(authHeader == null || authHeader.length() < 7){
            throw new CustomJWTException("INVALID STRING");
        }

        //Bearer xxxxxxxxxxxxxxxxx
        String accessToken = authHeader.substring(7);

        // AccessToken의 만료 여부 확인 -> method로 만들어 씀
        // 만료 안됐으면~ 기존 토큰 쓰세용
        if(checkExpiredToken(accessToken) == false){
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        //Refresh Token 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh ... clasims : " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60*24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

    }

    //시간이 1시간 미만으로 남았다면
    private boolean checkTime(Integer exp){

        //JWT exp를 날짜로 변환
        Date expDate = new Date((long) exp * 1000);

        //현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();

        //분단위 계산
        long leftMin = gap / (1000 * 60);

        //1시간도 안 남았는지...
        return leftMin < 60;

    }


    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        }catch(CustomJWTException ex){
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }

}
