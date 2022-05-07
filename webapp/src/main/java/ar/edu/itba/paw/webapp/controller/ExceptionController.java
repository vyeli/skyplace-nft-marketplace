package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.InvalidIdException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidIdException.class)
    public ModelAndView invalidId() {
        return new ModelAndView("error/404");
    }

}
