package com.sezanmahmud.hotel_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
public class User {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String password;
    private String cellNo;
    private String adress;
    private Date dob;
    private String gender;
    private String image;

    private boolean active;
    private boolean isLock;

}
