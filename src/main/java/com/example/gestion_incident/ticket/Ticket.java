package com.example.gestion_incident.ticket;

import com.example.gestion_incident.categorie.Categorie;
import com.example.gestion_incident.referentiel.Referentiel;
import com.example.gestion_incident.user.User;
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
    @JoinColumn( nullable = false)
    private User user;

    // Un ticket ne contient qu'une seule catégorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    // Un ticket pointe optionnellement vers un élément du référentiel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referentiel_id")
    private Referentiel referentiel;
}
