package com.example.festisounds.Modules.ContactForm.Controllers;

import com.example.festisounds.Modules.ContactForm.Entities.ContactForm;
import com.example.festisounds.Modules.ContactForm.Services.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactFormController {

    @Autowired
    private ContactFormService contactFormService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody ContactForm contactForm) {
        try {
            contactFormService.sendEmail(contactForm);
            return new ResponseEntity<>("Email sent", HttpStatus.OK);
        } catch (MailException e) {
            return new ResponseEntity<>("Error in sending email", HttpStatus.BAD_REQUEST);
        }
    }

}
