package ar.edu.itba.paw.exceptions;

public class InvalidReviewException extends RuntimeException{

    private static final String MESSAGE = "Invalid Review";

    public InvalidReviewException(){
        super(MESSAGE);
    }
}
