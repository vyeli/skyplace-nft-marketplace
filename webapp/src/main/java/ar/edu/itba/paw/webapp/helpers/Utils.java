package ar.edu.itba.paw.webapp.helpers;

import ar.edu.itba.paw.webapp.controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Objects;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static int parseInt(String number) throws NumberFormatException {
        int parsedNumber;
        parsedNumber = Integer.parseInt(number);
        return parsedNumber;
    }

    public static void setEncodingToUTF(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("utf-8");
        }
        catch(UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static double round(double number, int decimals){
        double rounder = Math.pow(10, decimals);
        return Math.round(number * rounder) / rounder;
    }

    public static String capitalizeString(String s) {
        if(s != null && s.length() > 0)
            return s.substring(0,1).toUpperCase().concat(s.substring(1));
        return s;
    }

    public static String getSortStringFormat(String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        if (!Objects.equals(locale.getLanguage(), "es")) {
            switch(sort) {
                case "priceAsc":
                    return "Price: Low to High";
                case "priceDsc":
                    return "Price: High to Low";
                case "collection":
                    return "Collection";
                default:
                    return "Name";
            }
        }
        switch(sort) {
            case "priceAsc":
                return "Precio: Menor a Mayor";
            case "priceDsc":
                return "Precio: Mayor a menor";
            case "collection":
                return "Colección";
            default:
                return "Nombre";
        }
    }
}
