package victor.training.sourcing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class) // @Validated
  public List<String> onJavaxValidationException(MethodArgumentNotValidException e) {
    List<String> validationErrors = e.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .toList();
    log.error("Invalid request: {}", validationErrors, e);
    return validationErrors;
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class) // optional.get() on empty Optional
  public String onNoSuchElementException(NoSuchElementException e) {
    log.error("Not Found", e);
    return "Not Found!";
  }

  @ExceptionHandler(Exception.class)
  public String onAnyOtherException(Exception exception) {
    log.error("Error", exception);
    return exception.toString();
  }

}