package com.empoweru.empowerupasswordrecoveryservice.exceptions;

/**
 * Custom exception class to handle scenarios where a provided recovery code is invalid.
 * This exception is thrown to indicate that the recovery process cannot proceed with the given code.
 */
public class InvalidRecoveryCode extends Exception {
    public InvalidRecoveryCode() {
        super("Invalid recovery code!");
    }
}
