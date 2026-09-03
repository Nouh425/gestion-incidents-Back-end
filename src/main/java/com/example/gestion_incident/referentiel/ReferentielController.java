package com.example.gestion_incident.referentiel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/referentiels")
public class ReferentielController {

    private final ReferentielService referentielService;

    public ReferentielController(ReferentielService referentielService) {
        this.referentielService = referentielService;
    }

    // CREATE : POST /api/referentiels
    @PostMapping
    public ResponseEntity<ReferentielDTO> create(@RequestBody ReferentielDTO dto) {
        ReferentielDTO created = referentielService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // READ ALL : GET /api/referentiels
    @GetMapping
    public ResponseEntity<List<ReferentielDTO>> getAll() {
        return ResponseEntity.ok(referentielService.getAll());
    }

    // READ BY ID : GET /api/referentiels/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReferentielDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(referentielService.getById(id));
    }

    // READ ENFANTS : GET /api/referentiels/{id}/children
    @GetMapping("/{id}/children")
    public ResponseEntity<List<ReferentielDTO>> getChildren(@PathVariable Long id) {
        return ResponseEntity.ok(referentielService.getChildrenByParentId(id));
    }

    // UPDATE : PUT /api/referentiels/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ReferentielDTO> update(@PathVariable Long id, @RequestBody ReferentielDTO dto) {
        return ResponseEntity.ok(referentielService.update(id, dto));
    }

    // DELETE : DELETE /api/referentiels/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        referentielService.delete(id);
        return ResponseEntity.noContent().build();
    }
}