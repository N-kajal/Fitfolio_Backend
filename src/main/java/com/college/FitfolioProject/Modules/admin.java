package com.college.FitfolioProject.Modules;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Table(name = "admin")
public class admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // auto-generated admin table id

    @Column(name = "user_id")
    private Long userid;      // jobseeker / recruiter id

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;


}
