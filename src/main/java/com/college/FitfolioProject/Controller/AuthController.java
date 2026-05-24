package com.college.FitfolioProject.Controller;

import com.college.FitfolioProject.Modules.Job_Seeker;
import com.college.FitfolioProject.Modules.Recruiter;

import com.college.FitfolioProject.Repository.Job_Seeker_Repository;
import com.college.FitfolioProject.Repository.Recruiter_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins ={"http://localhost:5173", "https://fitfolio-frontend.vercel.app" })
public class AuthController {

    @Autowired
    private Job_Seeker_Repository jobSeekerRepo;

    @Autowired
    private Recruiter_repository recruiterRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");
        String role = data.get("role");

        if ("JOB_SEEKER".equals(role)) {
            Job_Seeker js = jobSeekerRepo.findByUsername(username);

            if (js == null || !js.getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            return ResponseEntity.ok(Map.of(
                    "userId", js.getId(),
                    "role", js.getRole()
            ));
        }

        if ("RECRUITER".equals(role)) {
            Recruiter r = recruiterRepo.findByUsername(username);

            if (r == null || !r.getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            return ResponseEntity.ok(Map.of(
                    "userId", r.getId(),
                    "role", r.getRole()
            ));
        }

        return ResponseEntity.badRequest().body("Invalid role");
    }
}
