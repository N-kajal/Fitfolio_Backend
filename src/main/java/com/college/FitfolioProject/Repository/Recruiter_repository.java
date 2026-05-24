package com.college.FitfolioProject.Repository;

import com.college.FitfolioProject.Modules.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Recruiter_repository extends JpaRepository<Recruiter,Long> {

    List<Recruiter> findByCmpnameContainingIgnoreCase(String keyword);
    List<Recruiter> findByIndustryContainingIgnoreCase(String keyword);
    List<Recruiter> findByCityContainingIgnoreCase(String keyword);
    List<Recruiter> findByStateContainingIgnoreCase(String keyword);
    List<Recruiter> findByCountryContainingIgnoreCase(String keyword);
    List<Recruiter> findByRoleContainingIgnoreCase(String keyword);
    boolean existsByUsername(String username);
    Recruiter findByUsername(String username);
    Recruiter findByCmpemail(String cmpemail);

    Recruiter findByResetToken(String resetToken);



}
