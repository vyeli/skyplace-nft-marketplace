package ar.edu.itba.paw.exceptions;

public class ImageNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1335515183333566951L;
    private final static String MESSAGE = "Image does not exist";

    public ImageNotFoundException() {
        super(MESSAGE);
    }
}
