package com.college.FitfolioProject.Service;

import com.college.FitfolioProject.Modules.Job_Seeker;
import com.college.FitfolioProject.Modules.Recruiter;
import com.college.FitfolioProject.Modules.admin;
import com.college.FitfolioProject.Repository.Job_Repository;
import com.college.FitfolioProject.Repository.Job_Seeker_Repository;
import com.college.FitfolioProject.Repository.Recruiter_repository;
import com.college.FitfolioProject.Repository.admin_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class admin_service {

    @Autowired
    private Job_Seeker_Repository job_seeker_repository;

    @Autowired
    private Recruiter_repository recruiter_repository;

    @Autowired
    private admin_Repository admin_repository;

    public List<admin> syncAllUsers() {

        List<admin> result = new ArrayList<>();

        // 🔹 Job Seekers
        for (Job_Seeker js : job_seeker_repository.findAll()) {

            if (!admin_repository.existsByUseridAndRole(js.getId(), js.getRole())) {

                admin admin = new admin();
                admin.setUserid(js.getId());
                admin.setUsername(js.getUsername());
                admin.setEmail(js.getEmail());
                admin.setRole(js.getRole());

                result.add(admin_repository.save(admin)); // 🔥 SAVE
            }
        }

        // 🔹 Recruiters
        for (Recruiter rec : recruiter_repository.findAll()) {

            if (!admin_repository.existsByUseridAndRole(rec.getId(), rec.getRole())) {

                admin admin = new admin();
                admin.setUserid(rec.getId());
                admin.setUsername(rec.getUsername());
                admin.setEmail(rec.getCmpemail());
                admin.setRole(rec.getRole());

                result.add(admin_repository.save(admin)); // 🔥 SAVE
            }
        }

        return result;
    }

    public List<admin> getAllAdmins() {
        return admin_repository.findAll();
    }
}
