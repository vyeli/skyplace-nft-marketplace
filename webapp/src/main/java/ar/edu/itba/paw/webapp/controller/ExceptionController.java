//package ar.edu.itba.paw.webapp.controller;
//
//import ar.edu.itba.paw.exceptions.*;
//import ar.edu.itba.paw.webapp.exceptions.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.ModelAndView;
//
//
//@ControllerAdvice
//public class ExceptionController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);
//
//    @ExceptionHandler({NumberFormatException.class, NftNotFoundException.class, SellOrderNotFoundException.class, UserNotFoundException.class, ImageNotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView notFound(Exception e) {
//        LOGGER.error("{} redirecting to 404", e.getMessage());
//        return new ModelAndView("error/404");
//    }
//
//    @ExceptionHandler({UserNoPermissionException.class, UserIsNotNftOwnerException.class, NftAlreadyHasSellOrderException.class, SellOrderHasPendingBuyOrderException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ModelAndView noPermission(Exception e) {
//        LOGGER.error("{} redirecting to 403", e.getMessage());
//        return new ModelAndView("error/403");
//    }
//
//    @ExceptionHandler({UserNotLoggedInException.class})
//    public ModelAndView notLoggedIn(Exception e) {
//        LOGGER.error("{} redirecting to login", e.getMessage());
//        return new ModelAndView("frontcontroller/login");
//    }
//
//    @ExceptionHandler({CreateSellOrderException.class, InvalidChainException.class, InvalidCategoryException.class, CreateImageException.class, CreateUserException.class, InvalidInputTypeException.class, CreateNftException.class, InvalidReviewException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView internalError(Exception e) {
//        LOGGER.error("{} redirecting to 500", e.getMessage());
//        return new ModelAndView("error/500");
//    }
//}
