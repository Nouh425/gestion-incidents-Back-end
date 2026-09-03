package com.example.gestion_incident.referentiel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReferentielServiceImpl implements ReferentielService {

    private final ReferentielRepository referentielRepository;

    public ReferentielServiceImpl(ReferentielRepository referentielRepository) {
        this.referentielRepository = referentielRepository;
    }

    @Override
    public ReferentielDTO create(ReferentielDTO dto) {
        Referentiel referentiel = mapToEntity(dto);
        Referentiel saved = referentielRepository.save(referentiel);
        return mapToDTO(saved);
    }

    @Override
    public ReferentielDTO update(Long id, ReferentielDTO dto) {
        Referentiel referentiel = referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Référentiel non trouvé avec l'id : " + id));

        referentiel.setCode(dto.getCode());
        referentiel.setLibelle(dto.getLibelle());
        referentiel.setDescription(dto.getDescription());

        if (dto.getParentId() != null) {
            Referentiel parent = referentielRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent non trouvé avec l'id : " + dto.getParentId()));
            referentiel.setParent(parent);
        } else {
            referentiel.setParent(null);
        }

        Referentiel updated = referentielRepository.save(referentiel);
        return mapToDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ReferentielDTO getById(Long id) {
        Referentiel referentiel = referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Référentiel non trouvé avec l'id : " + id));
        return mapToDTO(referentiel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReferentielDTO> getAll() {
        return referentielRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReferentielDTO> getChildrenByParentId(Long parentId) {
        Referentiel parent = referentielRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent non trouvé avec l'id : " + parentId));
        return parent.getChildren().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!referentielRepository.existsById(id)) {
            throw new RuntimeException("Référentiel non trouvé avec l'id : " + id);
        }
        referentielRepository.deleteById(id);
    }

    // Méthodes utilitaires de mapping
    private ReferentielDTO mapToDTO(Referentiel entity) {
        ReferentielDTO dto = new ReferentielDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setLibelle(entity.getLibelle());
        dto.setDescription(entity.getDescription());
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }
        return dto;
    }

    private Referentiel mapToEntity(ReferentielDTO dto) {
        Referentiel entity = new Referentiel();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setLibelle(dto.getLibelle());
        entity.setDescription(dto.getDescription());
        if (dto.getParentId() != null) {
            Referentiel parent = referentielRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent non trouvé avec l'id : " + dto.getParentId()));
            entity.setParent(parent);
        }
        return entity;
    }
}