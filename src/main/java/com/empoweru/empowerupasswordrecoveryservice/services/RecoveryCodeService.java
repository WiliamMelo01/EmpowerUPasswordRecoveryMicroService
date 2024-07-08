package com.empoweru.empowerupasswordrecoveryservice.services;

import com.empoweru.empowerupasswordrecoveryservice.exceptions.ExpiredRecoveryCodeException;
import com.empoweru.empowerupasswordrecoveryservice.exceptions.InvalidRecoveryCode;
import com.empoweru.empowerupasswordrecoveryservice.models.RecoveryCode;
import com.empoweru.empowerupasswordrecoveryservice.repositories.RecoverCodeRepository;
import com.empoweru.empowerupasswordrecoveryservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * Service class for managing recovery codes used in password recovery processes.
 * This includes validating recovery codes, deleting them, and updating user passwords.
 */
@Service
@AllArgsConstructor
public class RecoveryCodeService {

    private final RecoverCodeRepository recoverCodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ZonedDateTime zonedDateTime;

    /**
     * Validates if a given recovery code is valid for the specified email.
     * Checks if the code exists and has not expired.
     *
     * @param email The email associated with the recovery code.
     * @param code The recovery code to validate.
     * @return true if the code is valid, false otherwise.
     * @throws ExpiredRecoveryCodeException if the code has expired.
     * @throws InvalidRecoveryCode if the code is invalid or does not exist.
     */
    public boolean isVerificationCodeValid(String email, String code) throws ExpiredRecoveryCodeException, InvalidRecoveryCode {
        RecoveryCode recoveryCode = recoverCodeRepository.findByEmail(email)
                .orElseThrow(InvalidRecoveryCode::new);


        if (recoveryCode.getExpiryDate().before(Timestamp.from(zonedDateTime.toInstant()))) {
            throw new ExpiredRecoveryCodeException();
        }

        return recoveryCode.getCode().equals(code);
    }

    /**
     * Deletes all recovery codes associated with the given email.
     * This is typically used after a successful password recovery to clean up unused codes.
     *
     * @param email The email for which to delete all recovery codes.
     */
    @Transactional
    public void deleteAllByEmail(String email) {
        recoverCodeRepository.deleteAllByEmail(email);
    }

    /**
     * Updates the password for a user identified by their email.
     * The new password is hashed before being stored.
     *
     * @param email The email of the user whose password is to be updated.
     * @param password The new password to set for the user.
     */
    @Transactional
    public void updatePassword(String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.updatePassword(email, hashedPassword);
    }


}
