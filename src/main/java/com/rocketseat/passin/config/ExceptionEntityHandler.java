package com.rocketseat.passin.config;

import com.rocketseat.passin.domain.attendee.exceptions.AttendeNotFoundException;
import com.rocketseat.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.rocketseat.passin.domain.attendee.exceptions.EventFullException;
import com.rocketseat.passin.domain.checkin.exceptions.CheckInAlreadyExistException;
import com.rocketseat.passin.domain.event.exceptions.EventNotFoundException;
import com.rocketseat.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }

    @ExceptionHandler(AttendeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExist(AttendeeAlreadyExistException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistException.class)
    public ResponseEntity handleCheckInAlreadyExist(CheckInAlreadyExistException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
