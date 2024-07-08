package com.empoweru.empowerupasswordrecoveryservice.exceptions;

/**
 * Custom exception class to handle scenarios where a password recovery code has expired.
 * This exception is thrown to indicate that the recovery process cannot proceed with an expired code.
 */
public class ExpiredRecoveryCodeException extends Exception {
    public ExpiredRecoveryCodeException() {
        super("Recovery code has expired!");
    }
}
