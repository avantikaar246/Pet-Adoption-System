package collections;

import entities.AdoptionApplication;
import java.util.*;

public class AdoptionQueue {
    private PriorityQueue<AdoptionApplication> queue;
    
    public AdoptionQueue() {
        // Priority based on compatibility score (higher score = higher priority)
        this.queue = new PriorityQueue<>(
            (a1, a2) -> Double.compare(a2.getCompatibilityScore(), a1.getCompatibilityScore())
        );
    }
    
    public void addApplication(AdoptionApplication application) {
        queue.offer(application);
    }
    
    public AdoptionApplication getNextApplication() {
        return queue.poll();
    }
    
    public AdoptionApplication peekNextApplication() {
        return queue.peek();
    }
    
    public boolean removeApplication(String applicationId) {
        return queue.removeIf(app -> app.getApplicationId().equals(applicationId));
    }
    
    public List<AdoptionApplication> getAllApplications() {
        return new ArrayList<>(queue);
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public void displayQueue() {
        System.out.println("Adoption Applications Queue (by compatibility score):");
        int position = 1;
        for (AdoptionApplication app : queue) {
            System.out.printf("%d. %s (Score: %.1f%%)%n", 
                position++, app.getAdopter().getName(), app.getCompatibilityScore());
        }
    }
}
