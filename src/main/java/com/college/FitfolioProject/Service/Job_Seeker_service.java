package com.college.FitfolioProject.Service;

import ENUM.ApplicationStatus;
import com.college.FitfolioProject.Exception.DuplicateApplicationException;
import com.college.FitfolioProject.Modules.*;
import com.college.FitfolioProject.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class Job_Seeker_service {

    @Autowired
    private Job_Seeker_Repository jobSeekerRepository;
    @Autowired
    private Application_Repository application_repository;
    @Autowired
    private  Job_Repository job_repository;
    @Autowired
    private admin_Repository admin_repository;

    @Autowired
    private Recruiter_repository recruiterRepository;

    public List<Job_Seeker> findAll() {
        return jobSeekerRepository.findAll();
    }

    public boolean saveEntry(Job_Seeker savejobSeeker) {
        if (jobSeekerRepository.existsByUsername(savejobSeeker.getUsername())) {
            throw new RuntimeException("USERNAME_EXISTS");
        }
        savejobSeeker.setDate(LocalDateTime.now());
        Job_Seeker saved = jobSeekerRepository.save(savejobSeeker);
        admin adminUser = new admin();
        adminUser.setUserid(saved.getId());
        adminUser.setUsername(saved.getUsername());
        adminUser.setEmail(saved.getEmail());
        adminUser.setRole(saved.getRole());
        admin_repository.save(adminUser);
        return true;
    }


    public Job_Seeker  getEntry(long myid) {
            Job_Seeker entry = jobSeekerRepository.findById(myid).orElseThrow(() -> new RuntimeException("The id doesnt exist"));
            return entry;
    }

    public boolean deleteEntry(  long id) {
        if (jobSeekerRepository.existsById(id)) {
            jobSeekerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Job_Seeker updateEntry( Job_Seeker updateEntry , long myid) {
        Job_Seeker existing = jobSeekerRepository.findById(myid).orElseThrow(() -> new RuntimeException("Job Seeker not found "));

        if(updateEntry.getFull_name() !=null)
            existing.setFull_name(updateEntry.getFull_name());

        if(updateEntry.getPhone_number() != null)
            existing.setPhone_number(updateEntry.getPhone_number());

        if(updateEntry.getEducation() !=null)
            existing.setEducation(updateEntry.getEducation());

        if(updateEntry.getExperience() != null)
            existing.setExperience(updateEntry.getExperience());

        if(updateEntry.getSkills() != null)
            existing.setSkills(updateEntry.getSkills());

        if(updateEntry.getProjects() != null)
            existing.setProjects(updateEntry.getProjects());

        if(updateEntry.getJob_Prefernces() !=null)
            existing.setJob_Prefernces(updateEntry.getJob_Prefernces());

        if(updateEntry.getLinks() != null)
            existing.setLinks(updateEntry.getLinks());

        updateEntry.setDate(existing.getDate());

        if(updateEntry.getDob() != null)
            existing.setDob(updateEntry.getDob());

        return jobSeekerRepository.save(existing);

    }

    public List<Recruiter> getdetails( String keywords) {
        List<Recruiter> byCompany = recruiterRepository.findByCmpnameContainingIgnoreCase(keywords);
        List<Recruiter> byIndustry = recruiterRepository.findByIndustryContainingIgnoreCase(keywords);
        List<Recruiter> byCity = recruiterRepository.findByCityContainingIgnoreCase(keywords);
        List<Recruiter> byCountry = recruiterRepository.findByCountryContainingIgnoreCase(keywords);
        List<Recruiter> byState = recruiterRepository.findByStateContainingIgnoreCase(keywords);
        List<Recruiter> byPosition = recruiterRepository.findByRoleContainingIgnoreCase(keywords);

        Set<Recruiter> result = new HashSet<>();
        result.addAll(byCompany);
        result.addAll(byIndustry);
        result.addAll(byCity);
        result.addAll(byCountry);
        result.addAll(byState);
        result.addAll(byPosition);

        return new ArrayList<>(result);
    }



    @Transactional
    public Application applyForJobWithCv(
            Long jobSeekerId,
            Long jobId,
            MultipartFile cvFile
    ) throws Exception {
        Job job = job_repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Job_Seeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

        List<Application> existing =
                application_repository.findByJobAndJobSeeker(job, jobSeeker);

        if (!existing.isEmpty()) {
            throw new DuplicateApplicationException(
                    "You have already applied for this job"
            );
        }

        Application application = new Application();
        application.setJob(job);
        application.setJobSeeker(jobSeeker);
        application.setCreatedDate(LocalDate.now());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCv(cvFile.getBytes());
        application.setCvFileName(cvFile.getOriginalFilename());
        application.setCvContentType(cvFile.getContentType());

        return application_repository.save(application);
    }


    public Map<String, String> deleteApplication(Long jobSeekerId, Long applicationId) {

        Job_Seeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));


        Application application = application_repository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));


        if (!application.getJobSeeker().getId().equals(jobSeeker.getId())) {
            throw new RuntimeException("You are not allowed to delete this application");
        }


        application_repository.delete(application);


        Map<String, String> response = new HashMap<>();
        response.put("message", "Application deleted successfully");
        response.put("applicationId", applicationId.toString());
        response.put("jobSeekerId", jobSeekerId.toString());
        return response;
    }


    public List<Map<String, Object>> getAllApplications(Long jobSeekerId) {
        Job_Seeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

        List<Application> applications =
                application_repository.findByJobSeeker(jobSeeker);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Application app : applications) {
            Map<String, Object> map = new HashMap<>();

            Job job = app.getJob();
            Recruiter recruiter = job.getRecruiter();

            map.put("applicationId", app.getId());
            map.put("status", app.getStatus());
            map.put("createdDate", app.getCreatedDate());


            map.put("jobTitle", job.getTitle());


            map.put("companyName", recruiter.getCmpname());

            result.add(map);
        }

        return result;
    }




    public Map<String, Object> getJobDetailsForUser(Long jobId) {

        Job job = job_repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Recruiter r = job.getRecruiter();

        Map<String, Object> response = new HashMap<>();

        // 🔹 JOB DETAILS
        response.put("jobTitle", job.getTitle());
        response.put("jobDescription", job.getDescription());
        response.put("experience", job.getExperience());
        response.put("salary", job.getSalary());
        response.put("jobLocation", job.getLocation());
        response.put("createdDate", job.getCreatedDate());

        // 🔹 COMPANY DETAILS (Recruiter)
        response.put("companyName", r.getCmpname());
        response.put("companyDescription", r.getCmpdesc());
        response.put("companyEmail", r.getCmpemail());
        response.put("companyWebsite", r.getCmpwebsite());
        response.put("companyPhone", r.getCmpphone());
        response.put("companyFounded", r.getCmpfounded());
        response.put("companyAddress", r.getCmpaddress());
        response.put("industry", r.getIndustry());
        response.put("city", r.getCity());
        response.put("state", r.getState());
        response.put("country", r.getCountry());

        // 🔹 Recruiter Person Info (optional)
        response.put("recruiterName", r.getRecrname());
        response.put("recruiterPosition", r.getRecrposition());
        response.put("recruiterEmail", r.getRecremail());
        response.put("recruiterPhone", r.getRecrphone());

        return response;
    }

    public List<Map<String, Object>> searchJobsForUser(String keyword) {

        List<Job> jobs = job_repository
                .findByTitleContainingIgnoreCaseOrRecruiter_CmpnameContainingIgnoreCase(
                        keyword, keyword);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Job job : jobs) {
            Recruiter r = job.getRecruiter();

            Map<String, Object> map = new HashMap<>();
            map.put("jobId", job.getId());
            map.put("company", r.getCmpname());
            map.put("industry", r.getIndustry());
            map.put("position", job.getTitle());
            map.put("experience", job.getExperience());
            map.put("location", job.getLocation());

            result.add(map);
        }

        return result;
    }















}