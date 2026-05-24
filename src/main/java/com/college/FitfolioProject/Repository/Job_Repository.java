package com.college.FitfolioProject.Repository;

import com.college.FitfolioProject.Modules.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Job_Repository  extends JpaRepository<Job, Long> {

    List<Job> findByTitleContainingIgnoreCase(String keyword);
    List<Job> findByLocationContainingIgnoreCase(String keyword);
    List<Job> findByRecruiterId(Long recruiter_id);
    List<Job> findByTitleContainingIgnoreCaseOrRecruiter_CmpnameContainingIgnoreCase(
            String title, String cmpname);



}
