package ar.edu.itba.paw.webapp.exceptions;

public class CreateNftException extends RuntimeException {
    private final static long serialVersionUID = -4755951345108346751L;
    private final static String MESSAGE = "Error creating NFT";

    public CreateNftException() {
        super(MESSAGE);
    }
}
