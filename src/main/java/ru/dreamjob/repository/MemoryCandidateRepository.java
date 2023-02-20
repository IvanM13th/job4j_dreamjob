package ru.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.dreamjob.model.Candidate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger();

    private final ConcurrentHashMap<Integer, Candidate> candidates = new ConcurrentHashMap<>();

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

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.putIfAbsent(candidate.getId(), candidate);
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
                candidate.getDescription(),
                candidate.getCityId(),
                candidate.getFileId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        List<Candidate> listOfCandidates = new ArrayList<>();
        for (var candidate : candidates.values()) {
            listOfCandidates.add(Candidate.of(
                    candidate.getId(),
                    candidate.getName(),
                    candidate.getDescription(),
                    candidate.getCreationDate(),
                    candidate.getCityId(),
                    candidate.getFileId()
            ));
        }
        return listOfCandidates;
    }
}
