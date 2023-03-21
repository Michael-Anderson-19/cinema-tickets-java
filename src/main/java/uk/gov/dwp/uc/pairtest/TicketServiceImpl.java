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

    }

    private void makePayment(Long accountId, int amount) {
        ticketPaymentService.makePayment(accountId, amount);
    }

    private void reserveSeats(Long accountId, int numberOfSeats) {
        seatReservationService.reserveSeat(accountId, numberOfSeats);
    }

}
