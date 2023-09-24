package com.codecademy.goldmedal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import java.math.BigDecimal;

@Entity
@Table(name="GOLD_MEDAL")
public class GoldMedal {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="YEAR")
    private Integer year;

    @Column(name="CITY")
    private String city;

    @Column(name="SEASON")
    private String season;

    @Column(name="NAME")
    private String name;

    @Column(name="COUNTRY")
    private String country;

    @Column(name="GENDER")
    private String gender;

    @Column(name="SPORT")
    private String sport;

    @Column(name="DISCIPLINE")
    private String discipline;

    @Column(name="EVENT")
    private String event;


    public GoldMedal() {
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public GoldMedal(Integer year,
                     String city,
                     String season,
                     String name,
                     String country,
                     String gender,
                     String sport,
                     String discipline,
                     String event) {
        this.year = year;
        this.city = city;
        this.season = season;
        this.name = name;
        this.country = country;
        this.gender = gender;
        this.sport = sport;
        this.discipline = discipline;
        this.event = event;
    }
}
