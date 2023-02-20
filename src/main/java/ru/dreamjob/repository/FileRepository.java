package ru.dreamjob.repository;

import ru.dreamjob.model.File;

import java.util.Optional;

public interface FileRepository {

    File save(File file);

    Optional<File> findById(int id);

    boolean deleteById(int id);
}
