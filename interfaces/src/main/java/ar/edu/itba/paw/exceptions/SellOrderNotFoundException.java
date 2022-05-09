package ar.edu.itba.paw.exceptions;

public class SellOrderNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5364161417091606204L;
    private final static String MESSAGE = "Sell order does not exist";

    public SellOrderNotFoundException() {
        super(MESSAGE);
    }
}
