package entities;

import java.time.LocalDate;
import java.util.*;

public class AdoptionApplication {
    private String applicationId;
    private Adopter adopter;
    private Pet pet;
    private LocalDate applicationDate;
    private String status; // "pending", "approved", "rejected", "under_review"
    private double compatibilityScore;
    private String notes;
    
    public AdoptionApplication() {}
    
    public AdoptionApplication(String applicationId, Adopter adopter, Pet pet) {
        setApplicationId(applicationId);
        this.adopter = adopter;
        this.pet = pet;
        this.applicationDate = LocalDate.now();
        this.status = "pending";
    }
    
    public String getApplicationId() { return applicationId; }
    
    public void setApplicationId(String applicationId) {
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID cannot be null or empty");
        }
        this.applicationId = applicationId;
    }
    
    public Adopter getAdopter() { return adopter; }
    
    public void setAdopter(Adopter adopter) { this.adopter = adopter; }
    
    public Pet getPet() { return pet; }
    
    public void setPet(Pet pet) { this.pet = pet; }
    
    public LocalDate getApplicationDate() { return applicationDate; }
    
    public void setApplicationDate(LocalDate applicationDate) {
        if (applicationDate == null || applicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid application date");
        }
        this.applicationDate = applicationDate;
    }
    
    public String getStatus() { return status; }
    
    public void setStatus(String status) {
        List<String> validStatuses = Arrays.asList("pending", "approved", "rejected", "under_review");
        if (!validStatuses.contains(status.toLowerCase())) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status.toLowerCase();
    }
    
    public double getCompatibilityScore() { return compatibilityScore; }
    
    public void setCompatibilityScore(double compatibilityScore) {
        if (compatibilityScore < 0 || compatibilityScore > 100) {
            throw new IllegalArgumentException("Compatibility score must be between 0-100");
        }
        this.compatibilityScore = compatibilityScore;
    }
    
    public String getNotes() { return notes; }
    
    public void setNotes(String notes) { this.notes = notes; }
    
    @Override
    public String toString() {
        return String.format(
            "AdoptionApplication[ID: %s, Adopter: %s, Pet: %s, Score: %.1f%%, Status: %s]",
            applicationId, adopter.getName(), pet.getName(), compatibilityScore, status);
    }
}
