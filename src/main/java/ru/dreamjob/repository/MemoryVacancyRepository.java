package ru.dreamjob.repository;

import ru.dreamjob.model.Vacancy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryVacancyRepository implements VacancyRepository {

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer",
                "We are looking for low-to-now experience Java developer"));
        save(new Vacancy(0, "Junior Java Developer",
                "We are looking for Java developer with some experience"));
        save(new Vacancy(0, "Junior+ Java Developer",
                "Java developer with good understanding of Java core"));
        save(new Vacancy(0, "Middle Java Developer",
                "Spring, Thymeleaf, Collections"));
        save(new Vacancy(0, "Middle+ Java Developer",
                "Spring, docker, microservices"));
        save(new Vacancy(0, "Senior Java Developer",
                "We are looking for highly experienced Jedi knight "
                        + "who fought armies of droids "
                        + "and is able to mind control our clients "
                        + "to make us drastically rich"));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy)
                -> new Vacancy(oldVacancy.getId(),
                vacancy.getTitle(),
                vacancy.getDescription())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }

}