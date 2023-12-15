package com.example.festisounds.Modules.ContactForm.Controllers;

import com.example.festisounds.Modules.ContactForm.Entities.ContactForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactFormControllerTest {

    @InjectMocks
    ContactFormController contactFormController;

    @Mock
    JavaMailSender javaMailSender;

    @Test
    public void testSendEmail_withAllDetails_ShouldReturnOK() {
        // Arrange
        ContactForm contactForm = new ContactForm("Max Burrows", "maxajburrows@example.com", "1234567890", "Your app is great", "I love your app so much");

        // Act
        ResponseEntity<?> responseEntity = contactFormController.sendEmail(contactForm);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        assertEquals(ResponseEntity.ok("Email sent"), responseEntity);
    }

    @Test
    public void testSendEmail_withMissingPhoneNumber_ShouldReturnOK() {
        // Arrange
        ContactForm contactForm = new ContactForm("John Doe", "john.doe@example.com", null, "Subject", "Details");

        // Act
        ResponseEntity<?> responseEntity = contactFormController.sendEmail(contactForm);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
