package com.college.FitfolioProject.Service;

import com.college.FitfolioProject.Modules.Job;
import com.college.FitfolioProject.Modules.Recruiter;
import com.college.FitfolioProject.Repository.Job_Repository;
import com.college.FitfolioProject.Repository.Recruiter_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class Job_Service {

    @Autowired
    private Recruiter_repository recruiter_repository;

    @Autowired
     private Job_Repository job_repository;

    public Job postJob(Long recruiterID , Job job) {
        Recruiter recruiter=recruiter_repository.findById(recruiterID).orElseThrow(() -> new RuntimeException("Recruiter not found"));

        job.setRecruiter(recruiter);
        job.setCreatedDate(LocalDate.now());

        return job_repository.save(job);
    }

    public Job getJobById(Long jobId) {
        return job_repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<Job> getAllJobs() {
        return job_repository.findAll();
    }

    public boolean deleteJob(Long jobId) {
        if (job_repository.existsById(jobId)) {
            job_repository.deleteById(jobId);
            return true;
        }
        return false;
    }

    public void deleteJobByRecruiter(Long jobId, Long recruiterId) {

        Job job = job_repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not allowed to delete this job");
        }

        job_repository.delete(job);
    }

    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return job_repository.findByRecruiterId(recruiterId);
    }
}
