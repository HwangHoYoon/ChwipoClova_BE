package com.chwipoClova.article.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 50)
    @NotNull
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Size(max = 100)
    @NotNull
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Size(max = 100)
    @NotNull
    @Column(name = "link", nullable = false, length = 100)
    private String link;

    @Column(name = "updated")
    private Instant updated;

    @Size(max = 100)
    @NotNull
    @Column(name = "copyright", nullable = false, length = 100)
    private String copyright;

    @NotNull
    @Column(name = "company_size", nullable = false)
    private Integer companySize;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rss_id", nullable = false)
    private Rss rss;

}