package system;

import java.util.*;
import java.io.*;

public class CustomerSupport implements CustomerService {
    private static final String FILE_PATH = "pet_adoption/tickets.csv";
    private List<Ticket> tickets = new ArrayList<>();
    private int nextTicketId = 1;

    public CustomerSupport() {
        loadTickets();
    }

    @Override
    public void raiseTicket(String adopterId, String issueDescription) {
        Ticket ticket = new Ticket(nextTicketId++, adopterId, issueDescription, "OPEN");
        tickets.add(ticket);
        saveTickets();
        System.out.println("[CustomerSupport] Ticket raised successfully: #" + ticket.getId());
    }

    @Override
    public void respondToTicket(int ticketId, String responseMessage) {
        for (Ticket t : tickets) {
            if (t.getId() == ticketId) {
                t.setResponse(responseMessage);
                t.setStatus("RESOLVED");
                saveTickets();
                System.out.println("[CustomerSupport] Responded to ticket #" + ticketId);
                return;
            }
        }
        System.out.println("Ticket ID not found.");
    }

    @Override
    public void viewAllTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return;
        }
        System.out.println("\n--- Support Tickets ---");
        for (Ticket t : tickets) {
            System.out.println(t);
        }
    }

    private void saveTickets() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Ticket t : tickets) {
                pw.printf("%d,%s,%s,%s,%s\n",
                    t.getId(), t.getAdopterId(), t.getIssue(), t.getResponse(), t.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving tickets: " + e.getMessage());
        }
    }

    private void loadTickets() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",", 5);
                if (data.length == 5) {
                    Ticket t = new Ticket(Integer.parseInt(data[0]), data[1], data[2], data[4]);
                    t.setResponse(data[3]);
                    tickets.add(t);
                    nextTicketId = Math.max(nextTicketId, t.getId() + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading tickets: " + e.getMessage());
        }
    }

    static class Ticket {
        private int id;
        private String adopterId;
        private String issue;
        private String response;
        private String status;

        public Ticket(int id, String adopterId, String issue, String status) {
            this.id = id;
            this.adopterId = adopterId;
            this.issue = issue;
            this.status = status;
            this.response = "";
        }

        public int getId() { return id; }
        public String getAdopterId() { return adopterId; }
        public String getIssue() { return issue; }
        public String getResponse() { return response; }
        public String getStatus() { return status; }

        public void setResponse(String r) { this.response = r; }
        public void setStatus(String s) { this.status = s; }

        @Override
        public String toString() {
            return String.format("Ticket #%d | Adopter: %s | Issue: %s | Response: %s | Status: %s",
                id, adopterId, issue, (response.isEmpty() ? "N/A" : response), status);
        }
    }
}
