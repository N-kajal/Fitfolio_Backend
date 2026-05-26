package com.college.FitfolioProject.Controller;

import ENUM.ApplicationStatus;
import com.college.FitfolioProject.Modules.Application;
import com.college.FitfolioProject.Modules.Job;
import com.college.FitfolioProject.Modules.Recruiter;
import com.college.FitfolioProject.Repository.Recruiter_repository;
import com.college.FitfolioProject.Service.Job_Service;
import com.college.FitfolioProject.Service.Recruiter_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.college.FitfolioProject.Modules.Job_Seeker;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = {"http://localhost:5173", "https://fitfolio-frontend.vercel.app" })
@RequestMapping("/Recruiter")
@RestController
public class Recruiter_controller {

    @Autowired
    private Recruiter_service recruiter_service;
    @Autowired
    private Recruiter_repository recruiter_repository;

    @Autowired
    private Job_Service job_service;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping
    public List<Recruiter> findAll() {
        return recruiter_service.findAll();
    }



    @DeleteMapping("/{id}")
    public boolean deleteRecruiter(@PathVariable Long id) {
        return recruiter_service.deleteEntry(id);
    }




    @PutMapping("/{id}")
    public Recruiter updateRecruiter(@PathVariable Long id, @RequestBody Recruiter updateRecruiter) {
        return recruiter_service.udpateEntry(updateRecruiter, id);
    }

    @PostMapping("/{recruiterId}/job")
    public Job postJob(@PathVariable Long recruiterId, @RequestBody Job job) {
        return recruiter_service.postJob(recruiterId, job);
    }

    @PutMapping("/{recruiterId}/job/{jobId}")
    public Job updateJob(@PathVariable Long recruiterId,
                         @PathVariable Long jobId,
                         @RequestBody Job job) {
        return recruiter_service.updateJobByRecruiter(recruiterId, jobId, job);
    }

    @GetMapping("/{recruiterId}/jobs")
    public Recruiter getRecruiterJobs(@PathVariable Long recruiterId) {
        return recruiter_service.getRecruiterWithJobs(recruiterId);
    }

    @DeleteMapping("/{recruiterId}/job/{jobId}")
    public String deleteJob(
            @PathVariable Long recruiterId,
            @PathVariable Long jobId
    ) {
        recruiter_service.deleteJob(recruiterId, jobId);
        return "Job deleted successfully";
    }

    @PutMapping("/application/status")
    public Application updateApplicationStatus(
            @RequestParam Long recruiterId,
            @RequestParam Long applicationId,
            @RequestParam ApplicationStatus status) {
        return recruiter_service.updateApplicationStatus(recruiterId, applicationId, status);
    }

    @GetMapping("/application/{applicationId}/cv")
    public ResponseEntity<byte[]> downloadCv(@PathVariable Long applicationId) {

        Application application = recruiter_service.getApplication(applicationId);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=\"" + application.getCvFileName() + "\"")
                .header("Content-Type", application.getCvContentType())
                .body(application.getCv());
    }
    @GetMapping("/job/{jobId}/applications")
    public List<Application> getApplicationsForJob(@PathVariable Long jobId) {
        return recruiter_service.getApplicationsForJob(jobId);
    }

    @PostMapping
    public ResponseEntity<?> registerRecruiter(@RequestBody Recruiter recruiter) {
        try {
            recruiter_service.saveEntry(recruiter);
            return ResponseEntity.ok("Recruiter Registered Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Recruiter getRecruiterById(@PathVariable Long id) {
        return recruiter_service.findByid(id);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email) {

        Recruiter user =
                recruiter_repository.findByCmpemail(email);

        if (user == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Recruiter not found");
        }

        String token =
                UUID.randomUUID().toString();

        user.setResetToken(token);

        user.setResetTokenExpiry(
                LocalDateTime.now().plusHours(1)
        );

        recruiter_repository.save(user);

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(user.getCmpemail());

        message.setSubject("Reset Password");

        message.setText(
                "Click below link to reset password:\n\n" +
                        "https://fitfolio-frontend.vercel.app/reset-password/recruiter/" + token
        );

        mailSender.send(message);

        return ResponseEntity.ok(
                "Reset email sent successfully"
        );
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(
            @PathVariable String token,
            @RequestBody Map<String, String> request
    ) {

        String newPassword = request.get("password");

        token = token.trim();

        Recruiter user =
                recruiter_repository.findByResetToken(token);
        System.out.println("TOKEN = " + token);
        System.out.println("USER = " + user);
        System.out.println("TOKEN FROM URL: " + token);
        System.out.println("USER FOUND: " + user);

        if (user == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid token");
        }

        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {

            return ResponseEntity
                    .badRequest()
                    .body("Token expired");
        }

        user.setPassword(newPassword);

        user.setResetToken(null);

        user.setResetTokenExpiry(null);

        recruiter_repository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password updated successfully"
                )
        );
    }


}
