package com.college.FitfolioProject.Repository;

import com.college.FitfolioProject.Modules.Job_Seeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Job_Seeker_Repository extends JpaRepository<Job_Seeker, Long> {
    boolean existsByUsername(String username);
    Job_Seeker findByUsername(String username);
    List<Job_Seeker> findByEmail(String email);

    Job_Seeker findByResetToken(String resetToken);


}
