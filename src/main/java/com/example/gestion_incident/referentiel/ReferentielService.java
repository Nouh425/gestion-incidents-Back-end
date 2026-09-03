package com.example.gestion_incident.referentiel;

import java.util.List;

public interface ReferentielService {
    ReferentielDTO create(ReferentielDTO dto);
    ReferentielDTO update(Long id, ReferentielDTO dto);
    ReferentielDTO getById(Long id);
    List<ReferentielDTO> getAll();
    List<ReferentielDTO> getChildrenByParentId(Long parentId);
    void delete(Long id);
}
