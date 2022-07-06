package hello.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//컴포넌트 스캔을 통해 스프링 빈으로 등록
// @Component
// @RequestMapping

@Controller
public class SpringMemberFormControllerV1 {

    // @Component 컴포넌트 스캔없이 스프링 빈으로 직접 등록해도 정상 동작
    // ServletApplication
    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form");
    }

}



