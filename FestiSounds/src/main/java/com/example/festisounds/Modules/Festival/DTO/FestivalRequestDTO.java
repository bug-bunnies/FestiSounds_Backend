package com.example.festisounds.Modules.Festival.DTO;

import lombok.Builder;

import java.util.Date;
import java.util.Set;

@Builder
public record FestivalRequestDTO(String name,
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
