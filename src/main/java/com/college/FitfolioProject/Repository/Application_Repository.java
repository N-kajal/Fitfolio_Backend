package com.college.FitfolioProject.Repository;

import com.college.FitfolioProject.Modules.Application;
import com.college.FitfolioProject.Modules.Job;
import com.college.FitfolioProject.Modules.Job_Seeker;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Registered
public interface Application_Repository extends JpaRepository<Application,Long> {

    List<Application> findByJobAndJobSeeker(Job job, Job_Seeker jobSeeker);
    List<Application> findByJobSeeker(Job_Seeker jobSeeker);

    List<Application> findByJob(Job job);
}
