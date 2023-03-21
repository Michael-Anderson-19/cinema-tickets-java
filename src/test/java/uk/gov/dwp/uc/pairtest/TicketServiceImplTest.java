package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicketServiceImplTest {


    private TicketServiceImpl ticketService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void given_ValidAccountIDAndValidTicketRequests_When_callingPurchaseTickets_Then_ReserveSeatsAndMakePaymentAreCalled() {

    }

    @Test
    public void given_InValidAccountID_When_callingPurchaseTickets_Then_ThrowException(){}

    @Test
    public void given_TicketRequestWithNoTickets_When_CallingPurchaseTickets_Then_ThrowException(){}

    @Test
    public void given_21TotalTickets_When_CallingPurchaseTickets_Then_ThrowException() {}

    @Test
    public void given_TicketRequestsWithNoAdult_When_CallingPurchaseTickets_Then_ThrowException(){}

    @Test
    public void given_InfantAndChildAndAnAdultTicketRequests_When_CallingPurchaseTickets_Then_InfantTicketsDoNotIncreasePrice(){}

    @Test
    public void given_InfantAndChildAndAdultTicketRequests_When_CallingPurchaseTickets_Then_InfantTicketsDoNotIncreaseTotalSeatCount() {}





}