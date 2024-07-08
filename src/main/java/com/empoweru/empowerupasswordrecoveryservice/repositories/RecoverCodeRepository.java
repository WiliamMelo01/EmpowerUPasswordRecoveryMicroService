package com.empoweru.empowerupasswordrecoveryservice.repositories;

import com.empoweru.empowerupasswordrecoveryservice.models.RecoveryCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Entity class representing a recovery code for password reset.
 * This class is used to store information about recovery codes generated for users attempting to reset their passwords.
 */
public interface RecoverCodeRepository extends CrudRepository<RecoveryCode, Long> {
    void deleteAllByEmail(String email);

    Optional<RecoveryCode> findByEmail(String email);

}
