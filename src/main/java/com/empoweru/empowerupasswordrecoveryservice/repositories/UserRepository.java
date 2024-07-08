package com.empoweru.empowerupasswordrecoveryservice.repositories;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Repository class for user-related database operations.
 * Utilizes the {@link EntityManager} for executing queries directly on the database.
 */
@Repository
@AllArgsConstructor
public class UserRepository {

    private final EntityManager entityManager;

    /**
     * Checks if a user exists by their email address.
     *
     * @param email The email address to check for existence.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    public boolean existsByEmail(String email) {
        return entityManager.createQuery("SELECT COUNT(u) > 0 FROM users u WHERE u.email = :email", Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    /**
     * Updates the password for a user identified by their email address.
     *
     * @param email The email address of the user whose password is to be updated.
     * @param password The new password to set for the user.
     */
    public void updatePassword(String email, String password) {
        entityManager.createQuery("UPDATE users u SET u.password = :password WHERE u.email = :email")
                .setParameter("password", password)
                .setParameter("email", email)
                .executeUpdate();
    }

}
