package com.example.gestion_incident.categorie;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'id : " + id));
    }

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorie(Long id, Categorie categorieDetails) {
        Categorie categorie = getCategorieById(id);
        categorie.setLibelle(categorieDetails.getLibelle());
        return categorieRepository.save(categorie);
    }

    public void deleteCategorie(Long id) {
        Categorie categorie = getCategorieById(id);
        categorieRepository.delete(categorie);
    }

}