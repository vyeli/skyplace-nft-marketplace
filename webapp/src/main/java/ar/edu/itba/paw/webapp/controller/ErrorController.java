package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @RequestMapping("/403")
    public ModelAndView accessDenied() {
        return new ModelAndView("error/403");
    }

    @RequestMapping("/404")
    public ModelAndView resourceNotFound() {
        return new ModelAndView("error/404");
    }

}
