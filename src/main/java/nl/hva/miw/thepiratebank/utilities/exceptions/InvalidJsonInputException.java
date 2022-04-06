package nl.hva.miw.thepiratebank.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidJsonInputException extends RuntimeException {
    public InvalidJsonInputException() {
        super("Invalid JSON input.");
    }
}
