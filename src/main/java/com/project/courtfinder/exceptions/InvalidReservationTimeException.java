package com.project.courtfinder.exceptions;

public class InvalidReservationTimeException extends RuntimeException {
    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
