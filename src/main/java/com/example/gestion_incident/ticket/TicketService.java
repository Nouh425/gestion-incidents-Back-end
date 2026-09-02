package com.example.gestion_incident.ticket;

import com.example.gestion_incident.categorie.Categorie;
import com.example.gestion_incident.categorie.CategorieRepository;
import com.example.gestion_incident.referentiel.Referentiel;
import com.example.gestion_incident.referentiel.ReferentielRepository;
import com.example.gestion_incident.user.User;
import com.example.gestion_incident.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CategorieRepository categorieRepository;
    private final ReferentielRepository referentielRepository;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            CategorieRepository categorieRepository,
            ReferentielRepository referentielRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categorieRepository = categorieRepository;
        this.referentielRepository = referentielRepository;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Ticket introuvable avec l'id : " + id
                ));
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {

        if (ticket.getCreatedBy() == null || ticket.getCreatedBy().getId() == null) {
            throw new RuntimeException("L'utilisateur créateur est obligatoire");
        }

        if (ticket.getAssignedTo() == null || ticket.getAssignedTo().getId() == null) {
            throw new RuntimeException("L'utilisateur assigné est obligatoire");
        }

        if (ticket.getCategorie() == null || ticket.getCategorie().getId() == null) {
            throw new RuntimeException("La catégorie est obligatoire");
        }

        User createdBy = findUser(
                Long.valueOf(ticket.getCreatedBy().getId()),
                "Utilisateur créateur introuvable"
        );

        User assignedTo = findUser(
                Long.valueOf(ticket.getAssignedTo().getId()),
                "Utilisateur assigné introuvable"
        );

        Categorie categorie = findCategorie(ticket.getCategorie().getId());

        ticket.setCreatedBy(createdBy);
        ticket.setAssignedTo(assignedTo);
        ticket.setCategorie(categorie);

        if (ticket.getReferentiel() != null && ticket.getReferentiel().getId() != null) {
            Referentiel referentiel = findReferentiel(ticket.getReferentiel().getId());
            ticket.setReferentiel(referentiel);
        } else {
            ticket.setReferentiel(null);
        }

        ticket.setCreatedate(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
    @Transactional
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = getTicketById(id);

        if (ticketDetails.getTitre() != null) {
            ticket.setTitre(ticketDetails.getTitre());
        }

        if (ticketDetails.getDescription() != null) {
            ticket.setDescription(ticketDetails.getDescription());
        }

        if (ticketDetails.getResolvedate() != null) {
            ticket.setResolvedate(ticketDetails.getResolvedate());
        }

        // Change creator only if sent
        if (ticketDetails.getCreatedBy() != null) {
            if (ticketDetails.getCreatedBy().getId() == null) {
                throw new RuntimeException("L'identifiant du créateur est obligatoire");
            }

            ticket.setCreatedBy(
                    findUser(
                            Long.valueOf(ticketDetails.getCreatedBy().getId()),
                            "Utilisateur créateur introuvable"
                    )
            );
        }

        // Change assigned technician only if sent
        if (ticketDetails.getAssignedTo() != null) {
            if (ticketDetails.getAssignedTo().getId() == null) {
                throw new RuntimeException("L'identifiant de l'utilisateur assigné est obligatoire");
            }

            ticket.setAssignedTo(
                    findUser(
                            Long.valueOf(ticketDetails.getAssignedTo().getId()),
                            "Utilisateur assigné introuvable"
                    )
            );
        }

        // Change category only if sent
        if (ticketDetails.getCategorie() != null) {
            if (ticketDetails.getCategorie().getId() == null) {
                throw new RuntimeException("L'identifiant de catégorie est obligatoire");
            }

            ticket.setCategorie(
                    findCategorie(ticketDetails.getCategorie().getId())
            );
        }

        // Change referential only if sent
        if (ticketDetails.getReferentiel() != null) {
            if (ticketDetails.getReferentiel().getId() == null) {
                throw new RuntimeException("L'identifiant du référentiel est obligatoire");
            }

            ticket.setReferentiel(
                    findReferentiel(ticketDetails.getReferentiel().getId())
            );
        }

        return ticketRepository.save(ticket);
    }
    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.delete(getTicketById(id));
    }

    private User findUser(Long id, String errorMessage) {
        return userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException(errorMessage));
    }

    private Categorie findCategorie(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));
    }

    private Referentiel findReferentiel(Long id) {
        return referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Référentiel introuvable"));
    }
}