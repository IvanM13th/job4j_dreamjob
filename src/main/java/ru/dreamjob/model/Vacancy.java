package ru.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vacancy {
    private int id;
    private String title;
    private String description;
    private LocalDateTime creationTime;

    public Vacancy(int id, String title, String description, LocalDateTime creationTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return id == vacancy.id && Objects.equals(title, vacancy.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}