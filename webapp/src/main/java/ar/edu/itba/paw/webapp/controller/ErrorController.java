package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    public ModelAndView accessDenied() {
        return new ModelAndView("error/403");
    }
}
