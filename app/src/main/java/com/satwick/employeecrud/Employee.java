package com.satwick.employeecrud;

import androidx.annotation.NonNull;

import java.util.Date;

public class Employee {
    private String name;
    private String id;
    private Date dateOfBirth;
    private String salary;
//    private Set<Integer> skillList;
//    private String imageDbUrl;

    public Employee() {

    }

    public Employee(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Employee(String name, String id, Date dob, String salary) {
        this.id = id;
        this.name = name;
//        this.dateOfBirth = dob;
//        this.salary = salary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setDateOfBirth(Date dateOfBirth) {
//        this.dateOfBirth = dateOfBirth;
//    }
//
//    public void setSalary(String salary) {
//        this.salary = salary;
//    }
//
//    public void setImageDbUrl(String imageDbUrl) {
//        this.imageDbUrl = imageDbUrl;
//    }
//
//    public void setSkillList(Set<Integer> skillList) {
//        this.skillList = skillList;
//    }
//
//    public void addSkill(int skill) {
//        this.skillList.add(skill);
//    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

//    public Set<Integer> getSkillList() {
//        return skillList;
//    }
//
//    public Date getDateOfBirth() {
//        return dateOfBirth;
//    }
//
//    public String getImageDbUrl() {
//        return imageDbUrl;
//    }
//
//    public String getSalary() {
//        return salary;
//    }

    @NonNull
    public String toString() {
        return "" + ("Name: " + this.name) + (", ID: " + this.id);
    }
}
