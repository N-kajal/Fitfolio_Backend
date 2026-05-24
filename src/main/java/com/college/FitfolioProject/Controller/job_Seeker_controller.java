package com.college.FitfolioProject.Controller;

import com.college.FitfolioProject.Exception.DuplicateApplicationException;
import com.college.FitfolioProject.Modules.Application;
import com.college.FitfolioProject.Modules.Job_Seeker;
import com.college.FitfolioProject.Modules.Recruiter;
import com.college.FitfolioProject.Repository.Job_Seeker_Repository;
import com.college.FitfolioProject.Service.Application_service;
import com.college.FitfolioProject.Service.Job_Seeker_service;
import com.college.FitfolioProject.Service.Job_Service;
import com.college.FitfolioProject.Service.Recruiter_service;
import com.college.FitfolioProject.Service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/Job_seeker")
@RestController
public class job_Seeker_controller {

    @Autowired
    private Job_Seeker_service jobSeekerService;

    @Autowired
    private Job_Service  jobService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Application_service applicationService;
    @Autowired
    private Job_Seeker_Repository Job_Seeker_repository;

    @Autowired Recruiter_service recruiterService;

    @Autowired
    private AIService aiService;

    @GetMapping
    public List<Job_Seeker> findjobseekerrecords() {
        return jobSeekerService.findAll();
    }

    @GetMapping("/{id}")
    public Job_Seeker findjobSeeker(@PathVariable long id) {
        return jobSeekerService.getEntry(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteJobSeeker(@PathVariable long id) {
        return jobSeekerService.deleteEntry(id);
    }

    @PostMapping
    public ResponseEntity<?> addjobseekerrecord(@RequestBody Job_Seeker jobSeeker) {
        if (Job_Seeker_repository.existsByUsername(jobSeeker.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists");
        }
        jobSeekerService.saveEntry(jobSeeker);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Registered successfully");
    }



    @PutMapping("{id}")
    public Job_Seeker updateJobSeeker(@RequestBody Job_Seeker update, @PathVariable long id) {
        return jobSeekerService.updateEntry(update,id);
    }


    @GetMapping("/getCmp_info")
    public  List<Recruiter> getCmp_info(@RequestParam String keyword )
    {
        return  jobSeekerService.getdetails(keyword);

    }



    @DeleteMapping("/application/delete")
    public Map<String, String> deleteApplication(@RequestParam Long jobSeekerId, @RequestParam Long applicationId) {
        return jobSeekerService.deleteApplication(jobSeekerId, applicationId);
    }

    @GetMapping("/applications")
    public List<Map<String, Object>> getAllApplications(@RequestParam Long jobSeekerId) {
        return jobSeekerService.getAllApplications(jobSeekerId);
    }


    @GetMapping("/job/details/{jobId}")
    public ResponseEntity<?> getJobDetails(@PathVariable Long jobId) {
        return ResponseEntity.ok(
                jobSeekerService.getJobDetailsForUser(jobId)
        );
    }

    @GetMapping("/search/jobs")
    public List<Map<String, Object>> searchJobs(@RequestParam String keyword) {
        return jobSeekerService.searchJobsForUser(keyword);
    }





    @PostMapping(
            value = "/apply-with-cv",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> applyWithCv(
            @RequestParam Long jobSeekerId,
            @RequestParam Long jobId,
            @RequestParam("cv") MultipartFile cvFile
    ) {
        try {
            if (cvFile.isEmpty()) {
                return ResponseEntity.badRequest().body("CV file is required");
            }

            Application application =
                    jobSeekerService.applyForJobWithCv(
                            jobSeekerId,
                            jobId,
                            cvFile
                    );

            return ResponseEntity.ok(application);

        } catch (DuplicateApplicationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to apply for job");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email) {

        List<Job_Seeker> users =
                Job_Seeker_repository.findByEmail(email);

        if (users.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Job Seeker not found");
        }

        Job_Seeker user = users.get(0);

        String token =
                UUID.randomUUID().toString();

        user.setResetToken(token);

        user.setResetTokenExpiry(
                LocalDateTime.now().plusHours(1)
        );

        Job_Seeker_repository.save(user);

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(user.getEmail());

        message.setSubject("Reset Password");

        message.setText(
                "Click below link to reset password:\n\n" +
                        "http://localhost:5173/reset-password/jobseeker/" + token
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

        Job_Seeker user =
                Job_Seeker_repository.findByResetToken(token);

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

        Job_Seeker_repository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password updated successfully"
                )
        );
    }

    @PostMapping("/ai-suggestions")
    public ResponseEntity<?> getAISuggestions(
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            @RequestParam(value = "textInput", required = false) String textInput
    ) {
        try {
            String inputData = "";
            if (resume != null && !resume.isEmpty()) {
                inputData = aiService.extractTextFromPdf(resume);
            } else if (textInput != null && !textInput.trim().isEmpty()) {
                inputData = textInput;
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Please provide a resume or text input"));
            }

            String suggestions = aiService.getSuggestions(inputData);
            return ResponseEntity.ok(Map.of("suggestions", suggestions));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process AI suggestions: " + e.getMessage()));
        }
    }
}
