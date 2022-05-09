package ar.edu.itba.paw.exceptions;

public class NftNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -3490670607594796602L;
    private final static String MESSAGE = "Nft does not exist";

    public NftNotFoundException() {
        super(MESSAGE);
    }
}
