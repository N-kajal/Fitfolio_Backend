package com.college.FitfolioProject.Modules;

import ENUM.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(
        name = "application_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"job_id", "jobseeker_id"})
        }
)
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 🔹 JOB
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnoreProperties({"applications", "recruiter"})
    private Job job;

    @ManyToOne
    @JoinColumn(name = "jobseeker_id", nullable = false)
    @JsonIgnoreProperties({"applications", "password"})
    private Job_Seeker jobSeeker;
    // 🔹 APPLICATION STATUS
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    // 🔹 APPLIED DATE
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Lob
    @Column(name = "cv", columnDefinition = "LONGBLOB")
    private byte[] cv;

    @Column(name = "cv_file_name")
    private String cvFileName;

    @Column(name = "cv_content_type")
    private String cvContentType;
}
