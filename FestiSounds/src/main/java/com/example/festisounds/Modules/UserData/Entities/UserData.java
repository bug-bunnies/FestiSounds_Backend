package com.example.festisounds.Modules.UserData.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class UserData {

    private UUID userId;

    private String spotifyUserId;

}
