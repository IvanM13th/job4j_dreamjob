package ru.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.dreamjob.model.Candidate;
import ru.dreamjob.repository.CandidateRepository;
import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
public class SimpleCandidateService  implements CandidateService {

    private final CandidateRepository candidateRepository;

    public SimpleCandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public boolean deleteById(int id) {
        return candidateRepository.deleteById(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidateRepository.update(candidate);
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }
}
