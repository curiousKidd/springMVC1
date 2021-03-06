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

### 서블릿과 JSP의 한계
서블릿으로 개발할 때는 뷰(View)화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했다.

JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용했다. 그런데 이렇게 해도 해결되지 않는 몇가지 고민이 남는다.

회원 저장 JSP를 보자. 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만 결과를 HTML로 보여주기 위한 뷰 영역이다. 회원 목록의 경우에도 마찬가지다.

코드를 잘 보면, JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다.
JSP가 너무 많은 역할을 한다. 이렇게 작은 프로젝트도 벌써 머리가 아파오는데, 수백 수천줄이 넘어가는 JSP를 떠올려보면 정말 지옥과 같을 것이다. (유지보수 지옥 썰)

### MVC 패턴의 등장
비즈니스 로직은 서블릿 처럼 다른곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)을 그리는 일에 집중하도록 하자. 과거 개발자들도 모두 비슷한 고민이 있었고, 그래서 MVC 패턴이 등장했다. 우리도 직접 MVC 패턴을 적용해서 프로젝트를 리팩터링 해보자.

## MVC 패턴 - 개요
### 너무 많은 역할
하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 모두 처리하게 되면, 너무 많은 역할을 하게되고, 결과적으로 유지보수가 어려워진다. 비즈니스 로직을 호출하는 부분에 변경이 발생해도 해당 코드를 손대야 하고, UI를 변경할 일이 있어도 비즈니스 로직이 함께 있는 해당 파일을 수정해야 한다.
HTML 코드 하나 수정해야 하는데, 수백줄의 자바 코드가 함께 있다고 상상해보라! 또는 비즈니스 로직을 하나 수정해야 하는데 수백 수천줄의 HTML 코드가 함께 있다고 상상해보라.
변경의 라이프 사이클 사실 이게 정말 중요한데, 진짜 문제는 둘 사이에 변경의 라이프 사이클이 다르다는 점이다. 예를 들어서 UI 를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분 서로에게 영향을 주지 않는다. 이렇게 변경의 라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은 유지보수하기 좋지 않다. 
(물론 UI가 많이 변하면 함께 변경될 가능성도 있다.)

### 기능 특화
특히 JSP 같은 뷰 템플릿은 화면을 렌더링 하는데 최적화 되어 있기 때문에 이 부분의 업무만 담당하는 것이 가장 효과적이다.

### Model View Controller
MVC 패턴은 지금까지 학습한 것 처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와 뷰(View)라는 영역으로 서로 역할을 나눈 것을 말한다. 웹 애플리케이션은 보통 이 MVC 패턴을 사용한다.
**컨트롤러** : HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
**모델** : 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
**뷰** : 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.

![image](https://user-images.githubusercontent.com/77770531/174759297-abb348b7-4e9f-4fb7-92fe-ebbe959ea51f.png)

![image](https://user-images.githubusercontent.com/77770531/174759356-be220835-e657-414c-a4ec-69000986015e.png)

![image](https://user-images.githubusercontent.com/77770531/174759388-561ddb6c-a5dd-491d-ae76-e26fb8b2b088.png)

## MVC 패턴 - 한계
MVC 패턴을 적용한 덕분에 컨트롤러의 역할과 뷰를 렌더링 하는 역할을 명확하게 구분할 수 있다.
특히 뷰는 화면을 그리는 역할에 충실한 덕분에, 코드가 깔끔하고 직관적이다. 단순하게 모델에서 필요한 데이터를 꺼내고, 화면을 만들면 된다.
그런데 컨트롤러는 딱 봐도 중복이 많고, 필요하지 않는 코드들도 많이 보인다

### MVC 컨트롤러의 단점

### 포워드 중복
View로 이동하는 코드가 항상 중복 호출되어야 한다. 물론 이 부분을 메서드로 공통화해도 되지만, 해당 메서드도 항상 직접 호출해야 한다.
``` java
  RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
  dispatcher.forward(request, response);
```

### ViewPath에 중복
``` java
  String viewPath = "/WEB-INF/views/new-form.jsp";
```

prefix: `/WEB-INF/views/`
suffix: `.jsp`
그리고 만약 jsp가 아닌 thymeleaf 같은 다른 뷰로 변경한다면 전체 코드를 다 변경해야 한다.

### 사용하지 않는 코드
다음 코드를 사용할 때도 있고, 사용하지 않을 때도 있다. 특히 response는 현재 코드에서 사용되지 않는다.
```
  HttpServletRequest request, HttpServletResponse response
```
그리고 이런 `HttpServletRequest` , `HttpServletResponse`를 사용하는 코드는 테스트 케이스를 작성하기도 어렵다.

### 공통 처리가 어렵다.
기능이 복잡해질 수 록 컨트롤러에서 공통으로 처리해야 하는 부분이 점점 더 많이 증가할 것이다. 
단순히 공통 기능을 메서드로 뽑으면 될 것 같지만, 결과적으로 해당 메서드를 항상 호출해야 하고, 실수로 호출하지 않으면 문제가 될 것이다. 그리고 호출하는 것 자체도 중복이다.
  
### 정리하면 공통 처리가 어렵다는 문제가 있다.
이 문제를 해결하려면 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 한다. 소위 수문장 역할을 하는 기능이 필요하다.  
프론트 컨트롤러(Front Controller) 패턴을 도입하면 이런 문제를 깔끔하게 해결할 수 있다. (입구를 하나로!)  
스프링 MVC의 핵심도 바로 이 프론트 컨트롤러에 있다.  
  
![image](https://user-images.githubusercontent.com/77770531/177956722-748a8292-6374-4e6d-ba10-89a72ebe2c09.png)

![1](https://user-images.githubusercontent.com/77770531/177957252-baa081e1-1137-4a59-887e-fcb5fd4f1235.png)

### FrontController 패턴 특징
- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
- 입구를 하나로!
- 공통 처리 가능
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨
