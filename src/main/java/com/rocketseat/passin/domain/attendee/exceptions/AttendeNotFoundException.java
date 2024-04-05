package com.rocketseat.passin.domain.attendee.exceptions;

public class AttendeNotFoundException extends RuntimeException {
    public AttendeNotFoundException(String message){
        super(message);
    }
}
