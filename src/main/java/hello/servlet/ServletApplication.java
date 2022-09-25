package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServletApplication.class, args);
    }

    //스프링 빈 직접 등록
    //    @Bean
    //    SpringMemberFormControllerV1 springMemberFormControllerV1() {
    //        return new SpringMemberFormControllerV1();
    //    }

}
