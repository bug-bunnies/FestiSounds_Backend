package com.example.festisounds.Modules.ContactForm.Entities;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ContactForm {

    @NonNull
    private String name;

    @NonNull
    private String email;

    private String phone;

    @NonNull
    private String subject;

    @NonNull
    private String details;


}
