package com.empoweru.empowerupasswordrecoveryservice.services;

import com.empoweru.empowerupasswordrecoveryservice.models.RecoveryCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Service class for sending emails related to password recovery.
 * Implements the {@link EmailSender} interface to provide functionality for sending recovery codes and notifications.
 */
@Service
@AllArgsConstructor
public class EmailSenderService implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final CodeGeneratorService codeGeneratorService;
    private final Logger logger = Logger.getLogger(EmailSenderService.class.getName());

    // Configured to use a fixed thread pool of 10 threads to manage concurrent execution of email sending operations.
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * Sends a recovery code to the specified email address.
     * Generates a new recovery code, formats an email message, and sends it asynchronously.
     *
     * @param email The email address to send the recovery code to.
     * @return A String indicating the result of the send operation.
     */
    @Override
    public String sendRecoveryCode(String email) {
        String subject = "Welcome to EmpowerU!";
        RecoveryCode recoverToken = codeGeneratorService.generate(email);

        String htmlContent = """
                <html>
                <body>
                    <h2>Dear User,</h2>
                    <p>We received a request to reset your password for your <b>EmpowerU</b> account.</p>
                    <p>Your password reset code is: <b>""" + recoverToken.getCode() + """
                    </b></p>
                    <p>If you did not request a password reset, please ignore this email or contact support if you have any concerns.</p>
                    <br/>
                    <p>Best regards,<br/>The EmpowerU Team</p>
                </body>
                </html>
                """;

        sendEmailAsync(email, subject, htmlContent);
        return "A recovery code has been sent to your email!";
    }

    /**
     * Sends a notification of successful password recovery to the specified email address.
     * Formats an email message and sends it asynchronously.
     *
     * @param email The email address to send the notification to.
     */
    public void sendPasswordRecoveredSuccessfully(String email) {
        String subject = "Password Recovered Successfully!";
        String htmlContent = """
                <html>
                <body>
                    <h2>Dear User,</h2>
                    <p>Your password has been successfully recovered for your <b>EmpowerU</b> account.</p>
                    <p>If you did not request a password reset, please contact support immediately.</p>
                    <br/>
                    <p>Best regards,<br/>The EmpowerU Team</p>
                </body>
                </html>
                """;

        sendEmailAsync(email, subject, htmlContent);
    }

    /**
     * Asynchronously sends an email with the specified content.
     * Submits the email sending task to an executor service for asynchronous execution.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param htmlContent The HTML content of the email.
     */
    private void sendEmailAsync(String to, String subject, String htmlContent) {
        executorService.submit(() -> {
            try {
                sendEmail(to, subject, htmlContent);
            } catch (MessagingException e) {
                this.logger.warning("Failed to send email. " + e.getMessage());
            }
        });
    }

    /**
     * Synchronously sends an email with the specified content.
     * Creates and sends a MIME message using JavaMailSender.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param htmlContent The HTML content of the email.
     * @throws MessagingException if there is a failure in sending the email.
     */
    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setReplyTo("noreply@noreply.com");
        helper.setSentDate(new java.util.Date());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

}
