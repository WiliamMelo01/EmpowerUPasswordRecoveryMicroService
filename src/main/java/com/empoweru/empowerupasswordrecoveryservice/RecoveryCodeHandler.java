package com.empoweru.empowerupasswordrecoveryservice;

import com.empoweru.empowerupasswordrecoveryservice.dtos.VerifyCodeDto;
import com.empoweru.empowerupasswordrecoveryservice.repositories.UserRepository;
import com.empoweru.empowerupasswordrecoveryservice.services.EmailSender;
import com.empoweru.empowerupasswordrecoveryservice.services.RecoveryCodeService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler class for Azure Functions related to recovery code operations.
 * This class provides endpoints for sending recovery codes to users and verifying recovery codes submitted by users.
 */
@Component
@AllArgsConstructor
public class RecoveryCodeHandler {

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final RecoveryCodeService recoveryCodeService;

    /**
     * Azure Function for sending a recovery code to a user's email.
     * Validates the provided email and sends a recovery code if the email exists in the system.
     *
     * @param request The HTTP request containing the user's email.
     * @param context The execution context of the Azure Function.
     * @return An HTTP response message indicating the result of the operation.
     */
    @FunctionName("sendRecoveryCode")
    public HttpResponseMessage sendRecoveryCode(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context){
        String email = request.getBody()
                .orElseThrow(() -> new IllegalArgumentException("Email not provided"));

        if (!userRepository.existsByEmail(email)) {
            return createResponse(request, "Email not found!", HttpStatus.NOT_FOUND);
        }

        return createResponse(request, emailSender.sendRecoveryCode(email), HttpStatus.OK);
    }

    /**
     * Azure Function for verifying a recovery code submitted by a user.
     * Validates the recovery code and updates the user's password if the code is valid.
     *
     * @param request The HTTP request containing the verification data (email, code, new password).
     * @param context The execution context of the Azure Function.
     * @return An HTTP response message indicating the result of the operation.
     */
    @FunctionName("verifyRecoveryCode")
    public HttpResponseMessage verifyRecoveryCode(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<VerifyCodeDto>> request,
            ExecutionContext context) {

        VerifyCodeDto requestBody = request.getBody().orElseThrow(() -> new IllegalArgumentException("Invalid request body"));
        String email = requestBody.getEmail();
        String code = requestBody.getCode();
        String password = requestBody.getPassword();

        if (email.isEmpty() || code.isEmpty() || password.isEmpty()) {
            return createResponse(request, "Email, code and password are required!", HttpStatus.BAD_REQUEST);
        }

        if (!userRepository.existsByEmail(email)) {
            return createResponse(request, "Email not found!", HttpStatus.NOT_FOUND);
        }

        try {
            if (!recoveryCodeService.isVerificationCodeValid(email, code)) {
                return createResponse(request, "Invalid recovery code!", HttpStatus.BAD_REQUEST);
            }
            recoveryCodeService.updatePassword(email, password);
            recoveryCodeService.deleteAllByEmail(email);
            emailSender.sendPasswordRecoveredSuccessfully(email);

            return createResponse(request, "Password updated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(request, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Helper method to create an HTTP response message.
     *
     * @param request The original HTTP request.
     * @param message The message to include in the response body.
     * @param status The HTTP status code for the response.
     * @return An HttpResponseMessage configured with the specified status and body.
     */
    private HttpResponseMessage createResponse(HttpRequestMessage request, String message, HttpStatus status) {
        return request.createResponseBuilder(status)
                .body(message)
                .header("Content-Type", "application/text")
                .build();
    }
}