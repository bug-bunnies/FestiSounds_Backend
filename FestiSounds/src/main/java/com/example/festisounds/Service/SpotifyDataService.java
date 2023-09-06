package com.example.festisounds.Service;

import java.util.HashMap;
import java.util.Map;

public interface SpotifyDataService  {

    Map<String, Long> userTopGenres() throws Exception;

}
