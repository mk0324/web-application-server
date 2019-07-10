# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다.
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다.

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답

- Closeable (JDK 7 이후 추가된 문법)

자원을 할당 받아온 후 다 사용하면 close 해주는 문법
사용하려는 Class 가 Closeable이라는 인터페이스를 implements 함

    try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
    
    } catch(	){
    
    }

- Java에서는 Stream data들을 읽기 편하도록 InputStream 이외의 Api 데이터들을 제공

InputStream을 InputStreamReader로 감싼 후 다시 InputStreamReader를 BufferedReader로 감쌀 수 있음
'java InputStream to BufferedReader' 키워드로 검색

### 요구사항 2 - get 방식으로 회원가입

- 발생할 수 있는 문제점

사용자가 입력한 정보가 노출 됨

### 요구사항 3 - post 방식으로 회원가입

- 발생할 수 있는 문제점

refresh 는 브라우저가 이전 요청정보를 유지하고 있다가 다시 요청하는 방식으로 작동
회원가입이 중복으로 발생할 수 있음

### 요구사항 4 - redirect 방식으로 이동

- redirect 방식으로 페이지 이동하는 것은 302 상태코드를 활용

302와 헤더에 딸려있는 Location헤더를 이용하여 이동할 url 지정
spring mvc -> redirect: 방식으로 사용

### 요구사항 5 - cookie

- 브라우저 페이지들 간 Cookie 정보 유지

서버에서 응답으로 Set-Cookie: logined=true 지정하면 브라우저가 Cookie를 읽은 후 서버에 재요청 할 때 포함

### 요구사항 6 - stylesheet 적용

- HTTP 는 무상태 프로토콜 : 각 요청 간 데이터 공유 못함
- Cookie : 로그인 상태 유무와 같은 상태 정보 공유를 위해 사용
- 클라이언트에 저장된 쿠키는 보안이슈 포함 -> 세션 사용(쿠키 기반)

### heroku 서버에 배포 후

-