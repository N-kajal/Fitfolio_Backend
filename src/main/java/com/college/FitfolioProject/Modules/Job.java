package com.college.FitfolioProject.Modules;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="job_details")
@Data
public
class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",  nullable = false)
    private  String title;

    @Column(name = "description")
    private  String description;

    @Column(name = "experience")
    private String experience;

    @Column(name = "salary")
    private String salary;

    @Column(name = "location")
    private String location;


    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    @JsonBackReference
    private  Recruiter recruiter;

    @Column(name = "createdDate")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"job"})
    private List<Application> applications;




}
