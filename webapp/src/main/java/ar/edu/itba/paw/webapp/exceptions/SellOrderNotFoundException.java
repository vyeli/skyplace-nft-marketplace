package ar.edu.itba.paw.webapp.exceptions;

public class SellOrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4439804381464928244L;

    public SellOrderNotFoundException(final String message) {
        super(message);
    }

}
