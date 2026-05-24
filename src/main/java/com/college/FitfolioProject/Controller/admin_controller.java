package com.college.FitfolioProject.Controller;

import com.college.FitfolioProject.Modules.admin;
import com.college.FitfolioProject.Service.admin_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "https://fitfolio-frontend.vercel.app" })
@RequestMapping("/admin")
@RestController
public class admin_controller {

    @Autowired
    private admin_service admin_service;

    @PostMapping("/sync")
    public List<admin> syncUsers() {
        return admin_service.syncAllUsers();
    }


    @GetMapping("/users")
    public List<admin> getAllUsers() {
        return admin_service.getAllAdmins();
    }

}
