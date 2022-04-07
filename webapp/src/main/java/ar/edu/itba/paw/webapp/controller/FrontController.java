package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.service.SellOrderService;
import ar.edu.itba.paw.webapp.form.SellNftForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class FrontController {

    private final SellOrderService ss;

    @Autowired
    public FrontController(SellOrderService ss) {
        this.ss = ss;
    }

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

    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("sellNftForm") final SellNftForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/sell");
        return mav;
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createForm(form);
        }

        final SellOrder order = ss.create(form.getName(), form.getPrice(), form.getDescription(), form.getImage(), form.getEmail());
        return new ModelAndView("redirect:/product/" + order.getId());
    }

    @RequestMapping("/**")
    public ModelAndView notFound() {
        final ModelAndView mav = new ModelAndView("frontcontroller/notfound");
        return mav;
    }

}
