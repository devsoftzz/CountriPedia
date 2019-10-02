package com.devsoft.countryapi_recyclersearch;

import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable {
    String name,alpha2code,capital,region,flag;
    Integer population,area;
    ArrayList<String> timezones;
    ArrayList<Currency> currencies;

    public Country(String name, String alpha2code, String capital, String region, String flag, Integer population, Integer area, ArrayList<String> timezones, ArrayList<Currency> currencies) {
        this.name = name;
        this.alpha2code = alpha2code;
        this.capital = capital;
        this.region = region;
        this.flag = flag;
        this.population = population;
        this.area = area;
        this.timezones = timezones;
        this.currencies = currencies;
    }
}
