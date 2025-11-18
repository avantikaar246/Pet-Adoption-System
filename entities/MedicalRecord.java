package entities;

import java.time.LocalDate;
import java.util.*;

public class MedicalRecord {
    private String recordId;
    private List<String> vaccinations;
    private List<String> currentMedications;
    private List<String> knownAllergies;
    private boolean isCurrentlySick;
    private String currentCondition;
    private boolean hasSpecialNeeds;
    private String specialNeedsDescription;
    private LocalDate lastCheckup;
    
    public MedicalRecord() {
        this.vaccinations = new ArrayList<>();
        this.currentMedications = new ArrayList<>();
        this.knownAllergies = new ArrayList<>();
    }
    
    public MedicalRecord(String recordId) {
        this();
        setRecordId(recordId);
    }
    
    public String getRecordId() { return recordId; }
    
    public void setRecordId(String recordId) {
        if (recordId == null || recordId.trim().isEmpty()) {
            throw new IllegalArgumentException("Record ID cannot be null or empty");
        }
        this.recordId = recordId;
    }
    
    public List<String> getVaccinations() { return vaccinations; }
    
    public void setVaccinations(List<String> vaccinations) { 
        this.vaccinations = vaccinations != null ? vaccinations : new ArrayList<>(); 
    }
    
    public List<String> getCurrentMedications() { return currentMedications; }
    
    public void setCurrentMedications(List<String> currentMedications) { 
        this.currentMedications = currentMedications != null ? currentMedications : new ArrayList<>(); 
    }
    
    public List<String> getKnownAllergies() { return knownAllergies; }
    
    public void setKnownAllergies(List<String> knownAllergies) { 
        this.knownAllergies = knownAllergies != null ? knownAllergies : new ArrayList<>(); 
    }
    
    public boolean isCurrentlySick() { return isCurrentlySick; }
    
    public void setCurrentlySick(boolean currentlySick) { 
        this.isCurrentlySick = currentlySick; 
    }
    
    public String getCurrentCondition() { return currentCondition; }
    
    public void setCurrentCondition(String currentCondition) { 
        this.currentCondition = currentCondition; 
    }
    
    public boolean hasSpecialNeeds() { return hasSpecialNeeds; }
    
    public void setHasSpecialNeeds(boolean hasSpecialNeeds) { 
        this.hasSpecialNeeds = hasSpecialNeeds; 
    }
    
    public String getSpecialNeedsDescription() { return specialNeedsDescription; }
    
    public void setSpecialNeedsDescription(String specialNeedsDescription) { 
        this.specialNeedsDescription = specialNeedsDescription; 
    }
    
    public LocalDate getLastCheckup() { return lastCheckup; }
    
    public void setLastCheckup(LocalDate lastCheckup) {
        if (lastCheckup != null && lastCheckup.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Last checkup cannot be in the future");
        }
        this.lastCheckup = lastCheckup;
    }
    
    public void addVaccination(String vaccination) {
        this.vaccinations.add(vaccination);
    }
    
    public void addMedication(String medication) {
        this.currentMedications.add(medication);
    }
    
    public void addAllergy(String allergy) {
        this.knownAllergies.add(allergy);
    }
    
    @Override
    public String toString() {
        return String.format("MedicalRecord[ID: %s, Sick: %s, Special Needs: %s, Last Checkup: %s]",
                           recordId, isCurrentlySick, hasSpecialNeeds, lastCheckup);
    }
}
