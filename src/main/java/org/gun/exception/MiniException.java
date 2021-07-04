package org.gun.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MiniException extends Exception {
    public MiniException(String message){
        super(message);
    }

}
