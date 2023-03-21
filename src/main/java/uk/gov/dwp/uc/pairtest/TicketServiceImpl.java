package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    protected static final int INFANT_TICKET_COST = 0;
    protected static final int CHILD_TICKET_COST = 10;
    protected static final int ADULT_TICKET_COST = 20;


    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (!validateAccount(accountId)) throw new InvalidPurchaseException("Invalid account");

        if (!validateTotalTicketCount(ticketTypeRequests))
            throw new InvalidPurchaseException("Invalid ticket request - number of tickets must be between 1 and 20");

        if (!validateAdultIsPresent(ticketTypeRequests))
            throw new InvalidPurchaseException("Invalid ticket request - All ticket requests must contain at least 1 adult");

        makePayment(accountId,
                calculateTotalRequestsCost(ticketTypeRequests));

        reserveSeats(accountId,
                calculateTotalSeats(ticketTypeRequests));
    }

    private void makePayment(Long accountId, int amount) {
        ticketPaymentService.makePayment(accountId, amount);
    }

    private void reserveSeats(Long accountId, int numberOfSeats) {
        seatReservationService.reserveSeat(accountId, numberOfSeats);
    }

    private boolean validateTotalTicketCount(TicketTypeRequest[] ticketTypeRequests) {
        int totalTickets = calculateTotalTickets(ticketTypeRequests);
        return ((totalTickets <= 20) && (totalTickets > 0));
    }

    private boolean validateAdultIsPresent(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .anyMatch(ticket -> ticket.getTicketType() == TicketTypeRequest.Type.ADULT);
    }

    private boolean validateAccount(Long accountId) {
        return accountId > 0;
    }

    private int calculateTotalTickets(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
    }

    private int calculateTotalSeats(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).filter(ticket ->
                        !ticket.getTicketType().equals(TicketTypeRequest.Type.INFANT))
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
    }

    private int calculateTotalRequestsCost(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).mapToInt(this::getTotalTicketCost).sum();
    }

    private int getTotalTicketCost(TicketTypeRequest ticket) {
        return getTicketCost(ticket.getTicketType()) * ticket.getNoOfTickets();
    }

    private int getTicketCost(TicketTypeRequest.Type type) {
        return switch (type) {
            case INFANT -> INFANT_TICKET_COST;
            case CHILD -> CHILD_TICKET_COST;
            case ADULT -> ADULT_TICKET_COST;
        };
    }


}
