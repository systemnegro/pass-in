package systemnegro.com.passin.domain.checkin.exceptions;

public class CheckInAlreadyExistsException extends RuntimeException{
    public CheckInAlreadyExistsException(String massage) {
        super(massage);
    }
}
