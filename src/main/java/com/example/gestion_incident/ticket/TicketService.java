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

        if (ticket.getUser() == null || ticket.getUser().getId() == null) {
            throw new RuntimeException("L'utilisateur est obligatoire");
        }

        if (ticket.getCategorie() == null || ticket.getCategorie().getId() == null) {
            throw new RuntimeException("La catégorie est obligatoire");
        }

        User user = userRepository.findById(ticket.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Categorie categorie = categorieRepository.findById(ticket.getCategorie().getId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        ticket.setUser(user);
        ticket.setCategorie(categorie);

        if (ticket.getReferentiel() != null
                && ticket.getReferentiel().getId() != null) {

            Referentiel referentiel = referentielRepository
                    .findById(ticket.getReferentiel().getId())
                    .orElseThrow(() -> new RuntimeException("Référentiel introuvable"));

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

        ticket.setTitre(ticketDetails.getTitre());
        ticket.setDescription(ticketDetails.getDescription());

        if (ticketDetails.getResolvedate() != null) {
            ticket.setResolvedate(ticketDetails.getResolvedate());
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }
}