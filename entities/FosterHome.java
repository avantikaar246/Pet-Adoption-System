package entities;

import java.time.LocalDate;
import java.util.*;

public class FosterHome {
    private String id;
    private String name;
    private String contactInfo;
    private int capacity;
    private Set<String> specializations; // ["sick", "elderly", "special_needs"]
    private int currentOccupancy;
    
    public FosterHome() {
        this.specializations = new HashSet<>();
    }
    
    public FosterHome(String id, String name, String contactInfo, int capacity, 
                     Set<String> specializations) {
        setId(id);
        setName(name);
        setContactInfo(contactInfo);
        setCapacity(capacity);
        this.specializations = specializations != null ? specializations : new HashSet<>();
        this.currentOccupancy = 0;
    }
    
    public String getId() { return id; }
    
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        this.id = id;
    }
    
    public String getName() { return name; }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
    
    public String getContactInfo() { return contactInfo; }
    
    public void setContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact info cannot be null or empty");
        }
        this.contactInfo = contactInfo;
    }
    
    public int getCapacity() { return capacity; }
    
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }
    
    public Set<String> getSpecializations() { return specializations; }
    
    public void setSpecializations(Set<String> specializations) { 
        this.specializations = specializations != null ? specializations : new HashSet<>(); 
    }
    
    public int getCurrentOccupancy() { return currentOccupancy; }
    
    public void setCurrentOccupancy(int currentOccupancy) {
        if (currentOccupancy < 0 || currentOccupancy > capacity) {
            throw new IllegalArgumentException("Invalid occupancy");
        }
        this.currentOccupancy = currentOccupancy;
    }
    
    public boolean hasSpace() {
        return currentOccupancy < capacity;
    }
    
    public boolean canAcceptPet(Pet pet) {
        if (!hasSpace()) return false;
        
        // Check if foster home specializes in this pet's condition
        if (pet.getMedicalRecord() != null && pet.getMedicalRecord().hasSpecialNeeds()) {
            return specializations.contains("special_needs");
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("FosterHome[ID: %s, Name: %s, Capacity: %d/%d, Specializations: %s]",
                           id, name, currentOccupancy, capacity, specializations);
    }
}
