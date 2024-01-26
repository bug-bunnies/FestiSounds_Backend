package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.Festival.Service.FestivalService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserArtistMatchingServiceImplMockitoTest {

    @Mock
    FestivalService festivalService;

    @Mock
    UserCachingServiceImpl cachingService;

    @Mock
    UserProcessingServiceImpl userProcessingService;


}