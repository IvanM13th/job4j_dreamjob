package ru.dreamjob.repository;

import ru.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryVacancyRepository implements VacancyRepository {

    private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "We are looking for low-to-now experience Java developer", LocalDateTime.now()));
        save(new Vacancy(0, "Junior Java Developer", "We are looking for Java developer with some eperience", LocalDateTime.now()));
        save(new Vacancy(0, "Junior+ Java Developer", "Java developer with good understanding of Java core", LocalDateTime.now()));
        save(new Vacancy(0, "Middle Java Developer", "Spring, Thymeleaf, Collections", LocalDateTime.now()));
        save(new Vacancy(0, "Middle+ Java Developer", "Spring, docker, microservices", LocalDateTime.now()));
        save(new Vacancy(0, "Senior Java Developer", "We are looking for highly experienced Jedi knight who fought armies of droids and is able to mind control our clients to make us drastically rich", LocalDateTime.now()));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
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
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), oldVacancy.getCreationTime())) != null;
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