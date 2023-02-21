package ru.dreamjob.repository;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import ru.dreamjob.configuration.DatasourceConfiguration;
import ru.dreamjob.model.Candidate;
import ru.dreamjob.model.File;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class Sql2oCandidateRepositoryTest {
    private static Sql2oCandidateRepository sql2oCandidateRepository;
    private static Sql2oFileRepository sql2oFileRepository;
    private static File file;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var input = Sql2oCandidateRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(input);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oCandidateRepository = new Sql2oCandidateRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "test");
        sql2oFileRepository.save(file);
    }

    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void clearCandidates() {
        var candidates = sql2oCandidateRepository.findAll();
        for (var candidate : candidates) {
            sql2oCandidateRepository.deleteById(candidate.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var creationTime = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "name", "description", creationTime, 1, file.getId()));
        var savedCandidate = sql2oCandidateRepository.findById(candidate.getId()).get();
        assertThat(savedCandidate).usingRecursiveComparison().isEqualTo(candidate);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var creationTime = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate1 = sql2oCandidateRepository.save(new Candidate(0, "name1", "description1", creationTime, 1, file.getId()));
        var candidate2 = sql2oCandidateRepository.save(new Candidate(0, "name2", "description2", creationTime, 1, file.getId()));
        var candidate3 = sql2oCandidateRepository.save(new Candidate(0, "name3", "description3", creationTime, 1, file.getId()));
        var rsl = sql2oCandidateRepository.findAll();
        assertThat(rsl).isEqualTo(List.of(candidate1, candidate2, candidate3));
    }

    @Test
    public void whenNotSavedThenNothingFound() {
        assertThat(sql2oCandidateRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oCandidateRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetOptionalEmpty() {
        var creationTime = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "name", "description", creationTime, 1, file.getId()));
        var isDeleted = sql2oCandidateRepository.deleteById(candidate.getId());
        var savedCandidate = sql2oCandidateRepository.findById(candidate.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedCandidate).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oCandidateRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var creationTime = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "name", "description", creationTime, 1, file.getId()));
        var updatedCandidate = new Candidate(
                candidate.getId(), "new title", "new description", creationTime.plusDays(1),
                 1, file.getId()
        );
        var isUpdated = sql2oCandidateRepository.update(updatedCandidate);
        var savedCandidate = sql2oCandidateRepository.findById(updatedCandidate.getId()).get();
        AssertionsForClassTypes.assertThat(isUpdated).isTrue();
        AssertionsForClassTypes.assertThat(savedCandidate).usingRecursiveComparison().isEqualTo(updatedCandidate);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        var creationTime = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = new Candidate(0, "title", "description", creationTime, 1, file.getId());
        var isUpdated = sql2oCandidateRepository.update(candidate);
        AssertionsForClassTypes.assertThat(isUpdated).isFalse();
    }

}
