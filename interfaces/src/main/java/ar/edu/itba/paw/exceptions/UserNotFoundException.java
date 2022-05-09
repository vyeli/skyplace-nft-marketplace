package ar.edu.itba.paw.exceptions;

public class UserNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 6547590290096537427L;
    private final static String MESSAGE = "User does not exist";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
