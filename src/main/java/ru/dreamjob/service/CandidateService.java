package ru.dreamjob.service;

import ru.dreamjob.dto.FileDto;
import ru.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

public interface CandidateService {
    Candidate save(Candidate candidate, FileDto image);

    boolean deleteById(int id);

    boolean update(Candidate candidate, FileDto image);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();
}
