package com.college.FitfolioProject.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {

    @GetMapping("/health-check")
    public String health() {
        return "OK";
    }
}
