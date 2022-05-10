package ar.edu.itba.paw.exceptions;

public class CreateImageException extends RuntimeException {
    private final static long serialVersionUID = -2792212395710500117L;
    private final static String MESSAGE = "Error creating image";

    public CreateImageException() {
        super(MESSAGE);
    }
}
