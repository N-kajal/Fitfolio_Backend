package com.college.FitfolioProject.Repository;

import com.college.FitfolioProject.Modules.admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface admin_Repository extends JpaRepository<admin, Long> {

    boolean existsByUseridAndRole(Long userid, String role);
}
