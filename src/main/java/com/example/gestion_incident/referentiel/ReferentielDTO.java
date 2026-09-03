package com.example.gestion_incident.referentiel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferentielDTO {
    private Long id;
    private String code;
    private String libelle;
    private String description;
    private Long parentId;
}