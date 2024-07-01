package com.chwipoClova.article.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "rss")
public class Rss {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "link", nullable = false, length = 100)
    private String link;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "ok")
    private Boolean ok;

    @Size(max = 100)
    @Column(name = "error", length = 100)
    private String error;

}