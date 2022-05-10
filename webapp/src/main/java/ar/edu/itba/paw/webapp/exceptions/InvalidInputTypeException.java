package ar.edu.itba.paw.webapp.exceptions;

public class InvalidInputTypeException extends RuntimeException {
    private final static long serialVersionUID = 9135181468342083857L;
    private final static String MESSAGE = "Invalid input type";

    public InvalidInputTypeException() {
        super(MESSAGE);
    }
}
