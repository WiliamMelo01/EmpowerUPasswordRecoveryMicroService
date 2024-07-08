package com.empoweru.empowerupasswordrecoveryservice.services;

import com.empoweru.empowerupasswordrecoveryservice.models.RecoveryCode;
import com.empoweru.empowerupasswordrecoveryservice.repositories.RecoverCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Random;


/**
 * Service class for generating recovery codes for password recovery.
 * This service handles the creation of unique recovery codes, setting their expiration times,
 * and managing their persistence in the database.
 */
@Service
@AllArgsConstructor
public class CodeGeneratorService {

    private final ZonedDateTime zonedDateTime;

    private final RecoverCodeRepository recoverCodeRepository;

    private final Random random = new Random();

    /**
     * Generates a new recovery code for a given email, deletes any existing codes for that email,
     * and saves the new code to the database.
     *
     * @param email The email address for which to generate a recovery code.
     * @return The saved RecoveryCode entity.
     */
    @Transactional
    public RecoveryCode generate(String email) {
        int code = random.nextInt(100000, 999999); // Generate a random 6-digit code.
        RecoveryCode recoverCode = new RecoveryCode();
        recoverCode.setCode(String.valueOf(code));
        recoverCode.setEmail(email);
        // Set the expiry date to 10 minutes from the current time.
        recoverCode.setExpiryDate(new Timestamp(zonedDateTime.toInstant().toEpochMilli() + 600000));
        recoverCodeRepository.deleteAllByEmail(email); // Delete any existing codes for this email.
        return recoverCodeRepository.save(recoverCode);
    }

}
