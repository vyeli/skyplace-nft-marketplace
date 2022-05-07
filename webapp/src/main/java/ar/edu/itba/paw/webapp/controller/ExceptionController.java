package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({NumberFormatException.class, NftNotFoundException.class, SellOrderNotFoundException.class, UserNotFoundException.class})
    public ModelAndView notFound() {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler({UserNoPermissionException.class, UserIsNotNftOwnerException.class})
    public ModelAndView noPermission() {
        return new ModelAndView("error/403");
    }

    @ExceptionHandler({UserNotLoggedInException.class})
    public ModelAndView notLoggedIn() {
        return new ModelAndView("frontcontroller/login");
    }

    @ExceptionHandler({CreateSellOrderException.class})
    public ModelAndView internalError() {
        return new ModelAndView("error/500");
    }
}
