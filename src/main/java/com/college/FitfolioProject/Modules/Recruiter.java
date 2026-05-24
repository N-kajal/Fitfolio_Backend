package com.college.FitfolioProject.Modules;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Recruiter_details")
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role",nullable = false)
    private String role;

    @Column(name="cmp_email",nullable = false, unique = true)
    private String cmpemail;

    @Column(name = "company_name")
    private String cmpname;

    @Column(name = "cmp_description")
    private String cmpdesc;

    @Column(name="cmp_websites")
    private String cmpwebsite;

    @Column(name = "cmp_address")
    private String cmpaddress;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="country")
    private String country;

    @Column(name = "cmp_phone")
    private String cmpphone;

    @Column(name="cmp_founded")
    private Date cmpfounded;

    @Column(name="industry")
    private String industry;

    @Column(name="recruiter_name")
    private String recrname;

    @Column(name = "recruiter_email")
    private String recremail;

    @Column(name = "recruiter_phone")
    private String recrphone;

    @Column(name="recruiter_position")
    private  String recrposition;

    @Column(name="Date")
    private LocalDateTime Date;

    @Column(name="Status")
    private String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Job> jobs;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;


}
