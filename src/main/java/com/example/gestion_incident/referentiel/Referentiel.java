package com.example.gestion_incident.referentiel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "referentiels")
@Getter
@Setter
public class Referentiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;
    private String libelle;

    // L'élément parent (peut être null si c'est la racine)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties("children")
    private Referentiel parent;

    // Liste des enfants générés par ce parent
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("parent")
    private List<Referentiel> children;
}