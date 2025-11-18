package system;

public interface CustomerService {
    void raiseTicket(String adopterId, String issueDescription);
    void respondToTicket(int ticketId, String responseMessage);
    void viewAllTickets();
}
