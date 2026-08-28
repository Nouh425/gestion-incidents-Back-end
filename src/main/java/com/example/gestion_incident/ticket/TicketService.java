package com.example.gestion_incident.ticket;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable avec l'id : " + id));
    }

    @Transactional // 👈 Added transactional context
    public Ticket createTicket(Ticket ticket) {
        ticket.setCreatedate(LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);

        // 👈 Force Hibernate to fully load the complete User details from DB before returning
        return ticketRepository.findById(savedTicket.getId()).orElseThrow();
    }

    @Transactional // 👈 Added transactional context
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = getTicketById(id);

        ticket.setTitre(ticketDetails.getTitre());
        ticket.setDescription(ticketDetails.getDescription());
        ticket.setCategorie(ticketDetails.getCategorie());
        ticket.setReferentiel(ticketDetails.getReferentiel());
        ticket.setUser(ticketDetails.getUser());

        if (ticketDetails.getResolvedate() != null) {
            ticket.setResolvedate(ticketDetails.getResolvedate());
        }

        Ticket updatedTicket = ticketRepository.save(ticket);

        // 👈 Force Hibernate to fully reload user/roles for the response payload
        return ticketRepository.findById(updatedTicket.getId()).orElseThrow();
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }
}
