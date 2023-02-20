package ru.dreamjob.dto;

public class FileDto {
    private String name;
    /**
     * главное различие - дто хранит содержимое (в данном случае в байтовом виде),
     * а доменная модель - хранить только путь к этому содержимому.
     */
    private byte[] content;

    public FileDto(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
