package com.codecademy.goldmedal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.goldmedal.model.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {
    public Optional<Country> findByName(String name);
    public List<Country> findAll();
}