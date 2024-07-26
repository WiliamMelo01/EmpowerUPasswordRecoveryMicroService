package com.empoweru.empowerupasswordrecoveryservice.services;

import jakarta.mail.MessagingException;

/**
 * Interface for email sending operations related to password recovery.
 * Defines methods for sending recovery codes and notifications of successful password recovery.
 */
public interface EmailSender {

    /**
     * Sends a recovery code to the specified email address.
     *
     * @param email The email address to send the recovery code to.
     * @return A String indicating the result of the send operation.
     */
    String sendRecoveryCode(String email);

    /**
     * Sends a notification to the specified email address indicating successful password recovery.
     *
     * @param email The email address to send the notification to.
     */
    void sendPasswordRecoveredSuccessfully(String email);

    /**
     * Asynchronously sends an email with the given subject and HTML content to the specified recipient.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param htmlContent The HTML content of the email.
     */
    void sendEmailAsync(String to, String subject, String htmlContent);

    /**
     * Synchronously sends an email with the given subject and HTML content to the specified recipient.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param htmlContent The HTML content of the email.
     */
    void sendEmail(String to, String subject, String htmlContent) throws MessagingException;

}
