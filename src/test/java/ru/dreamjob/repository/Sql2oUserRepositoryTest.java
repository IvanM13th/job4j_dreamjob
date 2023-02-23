package ru.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.dreamjob.configuration.DatasourceConfiguration;
import ru.dreamjob.model.User;

import java.util.List;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepo() throws Exception {
        var properties = new Properties();
        try (var input = Sql2oCandidateRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(input);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var config = new DatasourceConfiguration();
        var datasource = config.connectionPool(url, username, password);
        var sql2o = config.databaseClient(datasource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            String email = user.getEmail();
            sql2oUserRepository.deleteByEmail(email);
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "123@321.ru", "Ivan", "password")).get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSeveralUsersRegisteredThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(0, "123@321.ru", "Ivan", "password")).get();
        var user2 = sql2oUserRepository.save(new User(0, "123@322.ru", "Sergei", "password")).get();
        var user3 = sql2oUserRepository.save(new User(0, "123@323.ru", "Alex", "password")).get();
        var rsl = sql2oUserRepository.findAll();
        assertThat(rsl).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    public void whenDeleteThenGetOptionalEmpty() {
        var user1 = sql2oUserRepository.save(new User(0, "123@321.ru", "Ivan", "password")).get();
        var isDeleted = sql2oUserRepository.deleteByEmail(user1.getEmail());
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user1.getEmail(), user1.getPassword());
        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEqualTo(empty());
    }

    @Test
    public void whenEmailAlreadyExists() {
        var user1 = sql2oUserRepository.save(new User(0, "123@321.ru", "Ivan", "password")).get();
        var user2 = new User(0, "123@321.ru", "Ivan", "password");
        assertThatThrownBy(() -> sql2oUserRepository.save(user2))
                .isInstanceOf(Exception.class);
    }
}
