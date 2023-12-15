package com.example.festisounds.Modules.ContactForm.Controllers;

import com.example.festisounds.Modules.ContactForm.Entities.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactFormController {

    private final JavaMailSender javaMailSender;

    @Autowired
    public ContactFormController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody ContactForm contactForm) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            String RECEIVING_EMAIL_ADDRESS = "festisoundsapp@gmail.com";
            message.setTo(RECEIVING_EMAIL_ADDRESS);
            message.setSubject(contactForm.getSubject());
            message.setText("Name: " + contactForm.getName() + "\nEmail: " + contactForm.getEmail() + "\nPhone: " + contactForm.getPhone() + "\nDetails: " + contactForm.getDetails());

            javaMailSender.send(message);

            return new ResponseEntity<>("Email sent", HttpStatus.OK);
        } catch (MailException e) {
            return new ResponseEntity<>("Error in sending email", HttpStatus.BAD_REQUEST);
        }
    }

}
