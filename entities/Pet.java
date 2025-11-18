package entities;

import java.time.LocalDate;
import java.util.*;

public abstract class Pet {
    private String id;
    private String name;
    private String breed;
    private int age;
    private String gender;
    private LocalDate intakeDate;
    private boolean availableForAdoption;
    private MedicalRecord medicalRecord;
    private FosterHome currentFosterHome;
    
    public Pet() {
        this.availableForAdoption = true;
    }
    
    public Pet(String id, String name, String breed, int age, String gender) {
        setId(id);
        setName(name);
        setBreed(breed);
        setAge(age);
        setGender(gender);
        this.intakeDate = LocalDate.now();
        this.availableForAdoption = true;
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
    
    public String getBreed() { return breed; }
    
    public void setBreed(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            throw new IllegalArgumentException("Breed cannot be null or empty");
        }
        this.breed = breed;
    }
    
    public int getAge() { return age; }
    
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.age = age;
    }
    
    public String getGender() { return gender; }
    
    public void setGender(String gender) {
        List<String> validGenders = Arrays.asList("male", "female");
        if (!validGenders.contains(gender.toLowerCase())) {
            throw new IllegalArgumentException("Invalid gender");
        }
        this.gender = gender.toLowerCase();
    }
    
    public LocalDate getIntakeDate() { return intakeDate; }
    
    public void setIntakeDate(LocalDate intakeDate) {
        if (intakeDate == null || intakeDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid intake date");
        }
        this.intakeDate = intakeDate;
    }
    
    public boolean isAvailableForAdoption() { return availableForAdoption; }
    
    public void setAvailableForAdoption(boolean availableForAdoption) { 
        this.availableForAdoption = availableForAdoption; 
    }
    
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    
    public void setMedicalRecord(MedicalRecord medicalRecord) { 
        this.medicalRecord = medicalRecord; 
    }
    
    public FosterHome getCurrentFosterHome() { return currentFosterHome; }
    
    public void setCurrentFosterHome(FosterHome currentFosterHome) { 
        this.currentFosterHome = currentFosterHome;
        if (currentFosterHome != null) {
            this.availableForAdoption = false;
        }
    }
   
    public abstract String getPetType();
    public abstract double getDailyCareCost();
    public abstract int getSpaceRequirement();
    
    public boolean needsFosterCare() {
        return medicalRecord != null && 
               (medicalRecord.isCurrentlySick() || medicalRecord.hasSpecialNeeds());
    }
    
    @Override
    public String toString() {
        return String.format("Pet[ID: %s, Name: %s, Breed: %s, Age: %d, Gender: %s, Available: %s]",
                           id, name, breed, age, gender, availableForAdoption);
    }
}
