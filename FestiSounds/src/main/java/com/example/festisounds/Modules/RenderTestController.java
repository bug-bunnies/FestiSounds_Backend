package com.example.festisounds.Modules;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RenderTestController {

    @GetMapping("/test-render-update")
    public String testRenderUpdate() {
        return "This has been updated.";
    }
}
