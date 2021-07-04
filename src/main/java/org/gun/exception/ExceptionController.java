package org.gun.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@ControllerAdvice
public class ExceptionController {

    Logger logger = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler({
            MiniException.class
    })
    public ResponseEntity<Object> BadRequestException(final MiniException ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("content-type","application/json");
        return ResponseEntity.badRequest().headers(responseHeaders).body(ex.getMessage());
    }

    // 500 Error
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex) {
        logger.error("error", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}