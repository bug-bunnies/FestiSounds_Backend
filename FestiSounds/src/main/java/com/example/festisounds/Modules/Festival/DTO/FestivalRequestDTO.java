package com.example.festisounds.Modules.Festival.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.util.Date;
import java.util.Set;


@Builder
public record FestivalRequestDTO(
        @NotBlank
        String name,
                                 Date startDate,
                                 Date endDate,
                                 String details,
                                 Set<String> artists,
                                 String city,
                                 String country,
                                 String organizer,
                                 String website
) {
}
