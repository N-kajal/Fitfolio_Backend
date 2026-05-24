package com.college.FitfolioProject.Modules;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Data
@Table(name = "Job_Seeker_deatils")
public class Job_Seeker {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="email",nullable = false, unique = true)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="role",nullable = false)
    private String role;

    @Column(name = "fullname")
    private String full_name;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name="education")
    private String education;

    @Column(name = "experience")
    private String experience;

    @Column(name="skills")
    private String skills;

    @Column(name="projects")
    private String projects;

    @Column(name = "job-prefernce")
    private String job_Prefernces;

    @Column(name="links")
    private String links;

    @Column(name="Date")
    private LocalDateTime date;

    @Column (name="DOB")
    private Date dob;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Application> applications;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;


}
