package com.example.gestion_incident.ticket;

import com.example.gestion_incident.categorie.Categorie;
import com.example.gestion_incident.referentiel.Referentiel;
import com.example.gestion_incident.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @Column(name = "createdate")
    private LocalDateTime createdate;

    @Column(name = "resolvedate")
    private LocalDateTime resolvedate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referentiel_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Referentiel referentiel;

}
