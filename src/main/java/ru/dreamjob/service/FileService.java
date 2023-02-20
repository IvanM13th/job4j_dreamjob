package ru.dreamjob.service;

import ru.dreamjob.dto.FileDto;
import ru.dreamjob.model.File;

import java.util.Optional;

/**
 * сервис для ДТО
 */
public interface FileService {

    File save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    boolean deleteById(int id);
}
