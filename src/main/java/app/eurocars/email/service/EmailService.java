package app.eurocars.email.service;

import app.eurocars.web.dto.ChangedPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    //When password is changed that method is sending email
    //On setFrom is the email you added in application.properties
    //On set from is the email which profile password is changed
    //To work Avast security must be stopped
    @EventListener
    public void sendSimpleMessage(ChangedPasswordEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("");
        message.setTo("");
        message.setSubject("Changed password");
        message.setText("\tThe password to %s with email %s was changed on %s.\n\tIf it wasn't you, please contact us via: support@eurocars.eu".formatted(event.getOwner(), event.getEmail(), event.getChangeDate()));
        emailSender.send(message);
    }
}
