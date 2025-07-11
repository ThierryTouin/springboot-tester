package com.touin.thierry.sb.test.application.dto;

public class PigDTO {
    private Long id;
    private String name;

    public PigDTO() {
    }

    public PigDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
