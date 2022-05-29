package ar.edu.itba.paw.exceptions;

public class InvalidReviewException extends RuntimeException{
    private final static long serialVersionUID = 7504657963354878044L;
    private static final String MESSAGE = "Invalid Review";


    public InvalidReviewException(){
        super(MESSAGE);
    }
}
