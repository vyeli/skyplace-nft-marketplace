package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrontController {

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("frontcontroller/index");
        return mav;
    }

    @RequestMapping("/404")
    public ModelAndView notFound() {
        final ModelAndView mav = new ModelAndView("frontcontroller/notfound");
        return mav;
    }

}
