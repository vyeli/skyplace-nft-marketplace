package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrontController {

    @RequestMapping("/productDetail")
    public ModelAndView productDetail() {
        final ModelAndView mav = new ModelAndView("frontcontroller/product_detail");
        return mav;
    }
    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("frontcontroller/index");
        return mav;
    }

}
