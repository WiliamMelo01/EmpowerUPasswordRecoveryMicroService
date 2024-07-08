package com.empoweru.empowerupasswordrecoveryservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Entity class representing a recovery code for password reset.
 * This class is used to store information about recovery codes generated for users attempting to reset their passwords.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecoveryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String email;

    private Timestamp expiryDate;
}

