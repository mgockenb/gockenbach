package com.codecademy.goldmedal.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.goldmedal.model.GoldMedal;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Long> {
    public List<GoldMedal> findByCountry(String country);
    public List<GoldMedal> findByCountryAndSeason(String country, String season);
    public List<GoldMedal> findByCountryAndGender(String country, String gender);
    public List<GoldMedal> findBySeason(String season);
    public List<GoldMedal> findByYear(Integer year);

}