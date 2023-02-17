package ru.dreamjob.repository;

import ru.dreamjob.model.Candidate;
import ru.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Intern Java Developer",
                "I am looking for my first Java Developer Job"));
        save(new Candidate(0, "Junior Java Developer",
                "I know how to write loops and bubble sort!"));
        save(new Candidate(0, "Junior+ Java Developer",
                "I know how to work with different collections"));
        save(new Candidate(0, "Middle Java Developer",
                "I know what Spring is!"));
        save(new Candidate(0, "Middle+ Java Developer",
                "I am an experienced Java developer!"));
        save(new Candidate(0, "Senior Java Developer",
                "I'm Batman"));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate)
                -> new Candidate(
                oldCandidate.getId(),
                candidate.getName(),
                candidate.getDescription())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
