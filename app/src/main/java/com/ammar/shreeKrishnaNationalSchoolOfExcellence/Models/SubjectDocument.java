package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

import java.util.List;

public class SubjectDocument {

    public String id;
    public String name;
    public Long category;
    public List<Subject> subjects;

    public SubjectDocument() {
    }

    public SubjectDocument(String id, String name, Long category, List<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subjects = subjects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

}


