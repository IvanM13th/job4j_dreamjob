package ru.dreamjob.service;

import ru.dreamjob.model.City;
import java.util.Collection;

public interface CityService {
    Collection<City> findAll();
}
