# springMVC1
스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술

## 서블릿 컨테이너 동작 방식 설명

![내장톰켓서버생성](https://user-images.githubusercontent.com/77770531/170204261-ad9c9b69-c747-4200-8b32-f46967efda89.png)

![http](https://user-images.githubusercontent.com/77770531/170204286-b3712297-4d73-42c3-b4b3-3e3664b325ea.png)

![서버요청응답구조](https://user-images.githubusercontent.com/77770531/170204304-fffaeb81-0356-4066-a9f1-a0313f655fa8.png)

**참고**
HTTP 응답에서 Content-Length는 웹 애플리케이션 서버가 자동으로 생성해준다.

## HttpServletRequest

### HttpServletRequest 역할
HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다. 
서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다. 그리고 그 결과를 `HttpServletRequest` 객체에 담아서 제공한다.

HttpServletRequest를 사용하면 다음과 같은 HTTP 요청 메시지를 편리하게 조회할 수 있다.

### HTTP 요청 메시지
``` java
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded
username=kim&age=20
```

- START LINE
  - HTTP 메소드
  - URL
  - 쿼리 스트링
  - 스키마, 프로토콜
- 헤더
  - 헤더 조회
- 바디
  - form 파라미터 형식 조회
  - message body 데이터 직접 조회
HttpServletRequest 객체는 추가로 여러가지 부가기능도 함께 제공한다.

### 임시 저장소 기능
해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능
  - 저장: `request.setAttribute(name, value)`
  - 조회: `request.getAttribute(name)`
### 세션 관리 기능
- request.getSession(create: true)
> **중요**
> HttpServletRequest, HttpServletResponse를 사용할 때 가장 중요한 점은 이 객체들이 HTTP 요청 메시지, 
> HTTP 응답 메시지를 편리하게 사용하도록 도와주는 객체라는 점이다. 따라서 이 기능에 대해서 깊이있는 이해를 하려면 HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 한다.

### 참고
> 서블릿은 톰캣 같은 웹 애플리케이션 서버를 직접 설치하고, 그 위에 서블릿 코드를 클래스 파일로 빌드해서 올린 다음, 톰캣 서버를 실행하면 된다. 하지만 이 과정은 매우 번거롭다.
> 스프링 부트는 톰캣 서버를 내장하고 있으므로, 톰캣 서버 설치 없이 편리하게 서블릿 코드를 실행할 수 있다.

### 스프링 부트 서블릿 환경 구성
`@ServletComponentScan`
스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록 `@ServletComponentScan` 을 지원한다. 다음과 같이 추가하자.
``` java
@ServletComponentScan //서블릿 자동 등록
@SpringBootApplication
public class ServletApplication {
  public static void main(String[] args) {
    SpringApplication.run(ServletApplication.class, args);
  }
}
```

``` java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
 @Override
 protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   System.out.println("HelloServlet.service");
   System.out.println("request = " + request);
   System.out.println("response = " + response);
   String username = request.getParameter("username");
   System.out.println("username = " + username);
   response.setContentType("text/plain");
   response.setCharacterEncoding("utf-8");
   response.getWriter().write("hello " + username);
 }
}
```

`@WebServlet` 서블릿 어노테이션
- name: 서블릿 이름
- urlPatterns: URL 매핑

HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 다음 메서드를 실행한다.
`protected void service(HttpServletRequest request, HttpServletResponse response)`

해당 코드를 보면 controller와 상당히 유사하게 어노테이션을 사용하고 있는 것을 알 수 있다. `@Controller, @WebSerblet`
두개의 어노테이션의 차이점은
`@WebSerblet`
- `서블릿을 선언할 때` 사용되는 어노테이션이다
- 이 어노테이션이 표시된 클래스는 `Servlet Container에 의해 처리`된다
- 속성 값을 통해 해당 Servlet과 매칭될 URL 패턴을 지정한다.

`@Controller`
- 이 어노테이션이 표시된 클래스는 Controller 임을 나타낸다.
- `@Controller`는 `@Compomnent`의 구체화된 역할을 한다.
- classpath scanning을 통해 구현 클래스를 자동으로 감지할 수 있도록 해준다.
- 일반적으로 RequestMapping 어노테이션을 기반으로 한 handlerMethod와 함께 사용한다.

```
기본적으로 `@WebSerblet` 와 `@Controller` 는 같은 일을 하는데 사용된다
서블릿의 경우 J2EE 프레임워크의 일부이며, 모든 Java 어플리케이션 서버는 서블릿을 실행하기 위해서 빌드됩니다.
J2EE의 하위계층이 서블릿이기 때문에 사용하기 위한 특정한 파일은 필요하지 않습니다.
경우의 따라서는 (성능, 속도면의 차이) Java Servlet를 사용하는 것이 유리할 떄도 있습니다.
```
[설명](https://curiouskidd.tistory.com/9)
