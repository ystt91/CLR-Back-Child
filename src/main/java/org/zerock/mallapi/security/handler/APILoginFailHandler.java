package org.zerock.mallapi.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class APILoginFailHandler implements AuthenticationFailureHandler {

    // 실패 메시지를 200/400/500 뭘로 할거니?
    // 보통의 대기업들은 200으로 합니다.


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
    throws IOException, ServletException {

        log.info("Login fail..." + exception);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("error", "ERROR_LOGIN"));

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();

        /*
            주로 HTTP 응답을 작성하고 클라이언트에게 데이터를 전송하는 데 사용됩니다.
            Servlet은 Java EE 웹 애플리케이션에서 클라이언트의 요청을 처리하고,
            그에 대한 응답을 생성하는 서버 측 구성 요소입니다.
            응답은 일반적으로 HTML, JSON, XML 등의 형식으로 클라이언트에게 전송됩니다.
        */

    }
}
