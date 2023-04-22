package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicatedPlayerNameException.class)
    public ResponseEntity<Message> handleDuplicatedPlayerName(DuplicatedPlayerNameException ex, WebRequest request) {
        Message errorMessage = new Message(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Message> handlePlayerNotFound(PlayerNotFoundException ex, WebRequest request) {
        Message errorMessage = new Message(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyPlayerListException.class)
    public ResponseEntity<Message> handleEmptyPlayerList(EmptyPlayerListException ex, WebRequest request) {
        Message errorMessage = new Message(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

}
