package ru.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.dreamjob.model.Vacancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger();

    private final ConcurrentHashMap<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

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
        vacancy.setId(nextId.incrementAndGet());
        vacancies.putIfAbsent(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy)
                -> new Vacancy(
                oldVacancy.getId(),
                vacancy.getTitle(),
                vacancy.getDescription(),
                vacancy.getVisible())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        List<Vacancy> listOfVacancies = new ArrayList<>();
        for (var candidate : vacancies.values()) {
            listOfVacancies.add(Vacancy.of(
                    candidate.getId(),
                    candidate.getTitle(),
                    candidate.getDescription(),
                    candidate.getCreationTime(),
                    candidate.getVisible()
            ));
        }
        return listOfVacancies;
    }

}