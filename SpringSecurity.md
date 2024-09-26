


 _**- response.setContentType("application/json");_**
 - PrintWriter printWriter = response.getWriter();
 - printWriter.println(jsonStr);
 - printWriter.close();
        
 - PrintWriter
 - 주로 HTTP 응답을 작성하고 클라이언트에게 데이터를 전송하는 데 사용됩니다.
   Servlet은 Java EE 웹 애플리케이션에서 클라이언트의 요청을 처리하고,
   그에 대한 응답을 생성하는 서버 측 구성 요소입니다.
   응답은 일반적으로 HTML, JSON, XML 등의 형식으로 클라이언트에게 전송됩니다.

   - jwt 검사 필터 - > security로 설정
     -                어떤 경로로 설정할건지
       -              잘못되면 어떤 경로로 설정할건지ㄹ


 - AccessToken
    - 입장권
   
 - RefreshToken
    - 교환권
    - AccessToken과 같이 보낸다.
   
 - A가 없거나 잘못된 JWT인 경우 > 예외 메세지
 - A의 유효기간이 남은 경우 -> A 그대로 전송
 - A / R 모두 만료 -> 새로 로그인 필요
 - A만료, R 비만료 -> 새로운 A 발급
 - R이 거의 만료 ->   새로운 R 발급
                      (24시간 중 1시간 미만으로 남을 경우)
 - R의 유효기간 충분 -> 기존의 R 사용

 - 'api/member/refresh' 경로 활용


 - Spring은 자동으로 객체를 
