package cz.zcu.students.cacha.bp_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Class represent service which is responsible for mail operations
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Sends email with given parameters
     * @param to receiver of the email
     * @param subject subject of the email
     * @param text text of the email
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        // set email parameters
        message.setFrom("ctsnotifier1@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        // send mail
        emailSender.send(message);
    }
}
