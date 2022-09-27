package com.dadapp.seniorproject.user;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dadapp.seniorproject.role.Role;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Getter
@Setter
public class User {

    private Long id;

    private String email;

    private String zipcode;

    private String username;

    private String password;

    private String passwordConfirm;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String image;

    private String createdOn;

    private Integer age;

    private String fullName;

    private String description;

    private ArrayList<String> friendIds = new ArrayList<String>();

    private Set<Role> roles;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public User(Long id, String email, String zipcode, String username, String password, String firstName,
            String lastName, LocalDate dob, String image, Boolean online, Integer age, String fullName,
            String createdOn) {
        this.id = id;
        this.email = email;
        this.zipcode = zipcode;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.image = image;
        this.age = age;
        this.fullName = fullName;
        this.createdOn = createdOn;
    }

    public User(String email, String zipcode, String username, String password, String firstName, String lastName,
            LocalDate dob, String createdOn) {
        this.email = email;
        this.zipcode = zipcode;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.createdOn = createdOn;
    }

    @Transient
    public String getFullName(String firstName, String lastName) {
        return this.firstName + " " + this.lastName;
    }

    @Transient
    public Integer getAge(LocalDate dob) {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    @Transient
    public void setFullName() {
        this.fullName = getFullName(firstName, lastName);
    }

    @Transient
    public void setAge() {
        this.age = getAge(dob);
    }
}
