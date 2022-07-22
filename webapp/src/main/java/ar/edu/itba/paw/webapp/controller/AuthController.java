//package ar.edu.itba.paw.webapp.controller;
//
//import ar.edu.itba.paw.model.Chain;
//import ar.edu.itba.paw.model.User;
//import ar.edu.itba.paw.service.UserService;
//import ar.edu.itba.paw.webapp.auth.SkyplaceUserDetailsService;
//import ar.edu.itba.paw.webapp.form.UserForm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@Controller
//public class AuthController {
//
//    private final UserService userService;
//    private final SkyplaceUserDetailsService userDetailsService;
//
//    @Autowired
//    public AuthController(UserService userService, SkyplaceUserDetailsService userDetailsService) {
//        this.userService = userService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @RequestMapping("/login")
//    public ModelAndView login() {
//        return new ModelAndView("frontcontroller/login");
//    }
//
//    @RequestMapping( value = "/register", method = RequestMethod.GET)
//    public ModelAndView createUserForm(@ModelAttribute("userForm") final UserForm form) {
//        final ModelAndView mav = new ModelAndView("frontcontroller/register");
//        List<String> chains = Chain.getChains();
//        mav.addObject("chains", chains);
//        return mav;
//    }
//
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
//        if (errors.hasErrors())
//            return createUserForm(form);
//
//        final User user =  userService.create(form.getEmail(), form.getUsername(), form.getWalletAddress(), form.getWalletChain(), form.getPassword());
//
//        userDetailsService.autologin(user.getEmail(), form.getPassword());
//        return new ModelAndView("redirect:/");
//    }
//}
