package com.college.FitfolioProject.Service;

import com.college.FitfolioProject.Modules.Application;
import com.college.FitfolioProject.Modules.Job;
import com.college.FitfolioProject.Modules.Job_Seeker;
import com.college.FitfolioProject.Repository.Application_Repository;
import com.college.FitfolioProject.Repository.Job_Repository;
import com.college.FitfolioProject.Repository.Job_Seeker_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Application_service {

    @Autowired
    private Application_Repository application_repository;

    @Autowired
    private Job_Seeker_Repository job_seeker_repository;

    @Autowired
    private Job_Repository job_repository;




}
