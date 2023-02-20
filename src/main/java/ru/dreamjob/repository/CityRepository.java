package ru.dreamjob.repository;

import ru.dreamjob.model.City;
import java.util.Collection;

public interface CityRepository {

    Collection<City> findAll();
}
