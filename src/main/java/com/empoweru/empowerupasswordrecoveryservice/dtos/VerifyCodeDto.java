package com.empoweru.empowerupasswordrecoveryservice.dtos;

import lombok.Getter;

/**
 * Data Transfer Object (DTO) for verifying a code during password recovery.
 * This DTO is used to encapsulate the data required to verify a code sent to the user's email.
 */
@Getter
public class VerifyCodeDto {

    private String email;

    private String code;

    private String password;

}
