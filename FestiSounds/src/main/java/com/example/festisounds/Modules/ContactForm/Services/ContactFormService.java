package com.example.festisounds.Modules.ContactForm.Services;

import com.example.festisounds.Modules.ContactForm.Entities.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactFormService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public ContactFormService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(ContactForm contactForm) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("festisoundsapp@gmail.com");
        message.setSubject(contactForm.getSubject());
        message.setFrom(contactForm.getEmail());
        message.setText("Name: " + contactForm.getName() + "\nEmail: " + contactForm.getEmail() + "\nPhone: " + contactForm.getPhone() + "\nDetails: " + contactForm.getDetails());

        javaMailSender.send(message);
    }
}
