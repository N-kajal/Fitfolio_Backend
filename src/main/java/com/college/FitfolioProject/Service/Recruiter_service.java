package com.college.FitfolioProject.Service;


import ENUM.ApplicationStatus;
import com.college.FitfolioProject.Modules.Application;
import com.college.FitfolioProject.Modules.Job;
import com.college.FitfolioProject.Modules.Recruiter;
import com.college.FitfolioProject.Modules.admin;
import com.college.FitfolioProject.Repository.Application_Repository;
import com.college.FitfolioProject.Repository.Job_Repository;
import com.college.FitfolioProject.Repository.Recruiter_repository;
import com.college.FitfolioProject.Repository.admin_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.college.FitfolioProject.Modules.Job_Seeker;
import com.college.FitfolioProject.Repository.Job_Seeker_Repository;

@Service
public class Recruiter_service {

    @Autowired
    private Recruiter_repository recruiter_repository;

    @Autowired
    private Job_Repository jobRepository;

    @Autowired
    private Application_Repository application_repository;

    @Autowired
    private Job_Service job_service;

    @Autowired
    private admin_Repository admin_repository;

    @Autowired
    private Job_Seeker_Repository jobSeekerRepository;



    public List<Recruiter> findAll() {
        return recruiter_repository.findAll();
    }

    public boolean saveEntry(@RequestBody Recruiter entry) {
        if (recruiter_repository.existsByUsername(entry.getUsername())) {
            throw new RuntimeException("USERNAME_EXISTS");}
        entry.setDate(LocalDateTime.now());
        Recruiter saved =recruiter_repository.save(entry);
        admin adminUser = new admin();   // create admin object
        adminUser.setUserid(saved.getId());
        adminUser.setUsername(saved.getUsername());
        adminUser.setEmail(saved.getCmpemail());
        adminUser.setRole(saved.getRole());
        admin_repository.save(adminUser);
        return true;  }

    public Recruiter findByid( Long myid) {
        Recruiter entry  = recruiter_repository.findById(myid).orElseThrow(() ->
                new RuntimeException("The id doesn't exist"));
        return  entry;}

    public boolean deleteEntry( Long myid) {
        if (recruiter_repository.existsById(myid)) {
            recruiter_repository.deleteById(myid);
            return true;
        }
        return false;
    }

    public Recruiter udpateEntry( Recruiter updateEntry ,  Long myid) {
        Recruiter existing = recruiter_repository.findById(myid).orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (updateEntry.getCmpname() != null)
            existing.setCmpname(updateEntry.getCmpname());

        if(updateEntry.getCmpwebsite() != null)
            existing.setCmpwebsite(updateEntry.getCmpwebsite());

        if(updateEntry.getCmpaddress() !=null)
            existing.setCmpaddress(updateEntry.getCmpaddress());

        if(updateEntry.getCmpphone()!=null)
            existing.setCmpphone(updateEntry.getCmpphone());

        if(updateEntry.getCmpfounded()!=null)
            existing.setCmpfounded(updateEntry.getCmpfounded());

        if(updateEntry.getIndustry() != null)
            existing.setIndustry(updateEntry.getIndustry());

        if(updateEntry.getRecrname() != null)
            existing.setRecrname(updateEntry.getRecrname());

        if(updateEntry.getRecrphone() != null)
            existing.setRecrphone(updateEntry.getRecrphone());

        if(updateEntry.getRecremail() != null)
            existing.setRecremail(updateEntry.getRecremail());

        if(updateEntry.getRecrposition() != null)
            existing.setRecrposition(updateEntry.getRecrposition());

        updateEntry.setDate(existing.getDate());

        return  recruiter_repository.save(existing);
    }

    //Jobs

    public Job postJob(Long recruiterId, Job job) {

        Recruiter recruiter = recruiter_repository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter ID not found"));

        job.setRecruiter(recruiter);
        job.setCreatedDate(LocalDate.now());
        Job savedJob = jobRepository.save(job);

        return savedJob;
    }


    public Job updateJobByRecruiter(Long recruiterId, Long jobId, Job updatedJob) {

        Recruiter recruiter = recruiter_repository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter ID not found"));


        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job ID not found"));


        if (!existingJob.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("This job does not belong to this recruiter");
        }


        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setExperience(updatedJob.getExperience());
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setCreatedDate(LocalDate.now());


        return jobRepository.save(existingJob);
    }

    public Recruiter getRecruiterWithJobs(Long recruiterId) {
        return recruiter_repository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
    }

    public void deleteJob(Long recruiterId, Long jobId) {
        Recruiter recruiter = recruiter_repository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));


        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));


        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You are not allowed to delete this job");
        }

        jobRepository.delete(job);
    }

    public Application updateApplicationStatus(Long recruiterId, Long applicationId, ApplicationStatus status) {
        // Fetch the recruiter
        Recruiter recruiter = recruiter_repository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        // Fetch the application
        Application application = application_repository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        // Check if this recruiter owns the job for which the application is submitted
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You are not allowed to update this application");
        }
        // Update status
        application.setStatus(status);
        // Save and return updated application
        return application_repository.save(application);}

    public List<Application> getApplicationsForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return application_repository.findByJob(job);
    }

    public Application getApplication(Long applicationId) {
        return application_repository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }





}
