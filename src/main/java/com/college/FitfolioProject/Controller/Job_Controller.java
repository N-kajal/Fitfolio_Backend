package com.college.FitfolioProject.Controller;

import com.college.FitfolioProject.Modules.Job;
import com.college.FitfolioProject.Service.Job_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(originPatterns = {"http://localhost:5173", "https://*.vercel.app"})
@RequestMapping("/jobs")
@RestController
public class Job_Controller {

    @Autowired
    private  Job_Service job_service;

    @GetMapping("/jobs")
    public List<Job> getJobs() {
        return job_service.getAllJobs();
    }
}
