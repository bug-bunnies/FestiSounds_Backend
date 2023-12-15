package com.example.festisounds.Modules.ContactForm.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ContactForm {

    @JsonProperty
    @NonNull
    private String name;

    @JsonProperty
    @NonNull
    private String email;

    @JsonProperty
    private String phone;

    @JsonProperty
    @NonNull
    private String subject;

    @JsonProperty
    @NonNull
    private String details;


}
