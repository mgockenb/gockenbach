package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.CountriesResponse;
import com.codecademy.goldmedal.model.Country;
import com.codecademy.goldmedal.model.CountryDetailsResponse;
import com.codecademy.goldmedal.model.CountryMedalsListResponse;
import com.codecademy.goldmedal.model.CountrySummary;
import com.codecademy.goldmedal.model.GoldMedal;
import com.codecademy.goldmedal.model.Medals;
import com.codecademy.goldmedal.repositories.CountryRepository;
import com.codecademy.goldmedal.repositories.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.*;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private final GoldMedalRepository goldMedalRepository;
    private final CountryRepository countryRepository;

    public GoldMedalController(final GoldMedalRepository goldMedalRepository, final CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        List<GoldMedal> countryMedalsList = this.goldMedalRepository.findByCountry(countryName);

        switch (sortBy) {
            case "year":
                if (ascendingOrder) {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getYear)).collect(Collectors.toList());
                } else {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getYear).reversed()).collect(Collectors.toList());
                }
                break;
            case "season":
                if (ascendingOrder) {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getSeason)).collect(Collectors.toList());
                } else {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getSeason).reversed()).collect(Collectors.toList());
                }
                break;
            case "city":
                if (ascendingOrder) {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getCity)).collect(Collectors.toList());
                } else {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getCity).reversed()).collect(Collectors.toList());
                }
                break;
            case "name":
                if (ascendingOrder) {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getName)).collect(Collectors.toList());
                } else {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getName).reversed()).collect(Collectors.toList());
                }
                break;
            case "event":
                if (ascendingOrder) {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getEvent)).collect(Collectors.toList());
                } else {
                    medalsList = countryMedalsList.stream().sorted(Comparator.comparing(GoldMedal::getEvent).reversed()).collect(Collectors.toList());
                }
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        // get the country; this repository method should return a java.util.Optional
        Optional<Country> countryOptional = this.countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        // get the medal count
        var goldMedalCount = this.goldMedalRepository.findByCountry(countryName).size();

        // get the collection of wins at the Summer Olympics, sorted by year in ascending order.
        var summerWins =  this.goldMedalRepository.findByCountryAndSeason(countryName, "summer").
                stream().sorted(Comparator.comparing(GoldMedal::getYear)).collect(Collectors.toList());
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        // get the total number of events at the Summer Olympics
        var totalSummerEvents = this.goldMedalRepository.findBySeason("summer").size();
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        // get the collection of wins at the Winter Olympics
        var winterWins = this.goldMedalRepository.findByCountryAndSeason(countryName, "winter").
                stream().sorted(Comparator.comparing(GoldMedal::getYear)).collect(Collectors.toList());
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        // get the total number of events at the Winter Olympics, sorted by year in ascending order
        var totalWinterEvents = this.goldMedalRepository.findBySeason("winter").size();
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        // get the number of wins by female athletes
        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByCountryAndGender(countryName, "female").size();
        // get the number of wins by male athletes
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.findByCountryAndGender(countryName, "male").size();

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    // Sort the countries on name, GDP, population, number of medals.
    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        List<Country> allCountries = this.countryRepository.findAll();
        switch (sortBy) {
            case "name":
                // list of countries sorted by name in the given order
                if (ascendingOrder) {
                    countries = allCountries.stream().sorted(Comparator.comparing(Country::getName)).collect(Collectors.toList());
                } else {
                    countries = allCountries.stream().sorted(Comparator.comparing(Country::getName).reversed()).collect(Collectors.toList());
                }
                break;
            case "gdp":
                // list of countries sorted by gdp in the given order
                if (ascendingOrder) {
                    countries = this.countryRepository.findAll().stream().sorted(Comparator.comparing(Country::getGdp)).collect(Collectors.toList());
                } else {
                    countries = this.countryRepository.findAll().stream().sorted(Comparator.comparing(Country::getGdp).reversed()).collect(Collectors.toList());
                }
                break;
            case "population":
                // list of countries sorted by population in the given order
                if (ascendingOrder) {
                    countries = this.countryRepository.findAll().stream().sorted(Comparator.comparing(Country::getPopulation)).collect(Collectors.toList());
                } else {
                    countries = this.countryRepository.findAll().stream().sorted(Comparator.comparing(Country::getPopulation).reversed()).collect(Collectors.toList());
                }
                break;
            case "medals":
            default:
                // list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                countries = this.countryRepository.findAll();

                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            // get count of medals for the given country
            var goldMedalCount = this.goldMedalRepository.findByCountry(country.getName()).size();
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
