package ar.edu.itba.paw.exceptions;

public class CreateUserException extends RuntimeException{
    private final static long serialVersionUID = -2141022859274946129L;
    private final static String MESSAGE = "Error creating user";

    public CreateUserException() {
        super(MESSAGE);
    }
}
