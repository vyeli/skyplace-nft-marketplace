package ar.edu.itba.paw.webapp.exceptions;


public class InvalidIdException extends RuntimeException {

    private static final long serialVersionUID = -4439804381464928244L;

    public InvalidIdException(String message) {
        super(message);
    }

}
