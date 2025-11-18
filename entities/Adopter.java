package entities;

import java.time.LocalDate;
import java.util.*;

public class Adopter extends Person {
    private String housingType; // "apartment", "house", "condo"
    private boolean hasChildren;
    private int experienceWithPets; // years
    private double monthlyIncome;
    private List<String> lifestyle; // ["active", "homebody", "traveler"]
    private MedicalRecord medicalRecord;
    private String password; 
    
    public Adopter() {
        super();
        this.lifestyle = new ArrayList<>();
    }
    
    public Adopter(String id, String name, LocalDate dateOfBirth, String contactInfo,
                  String housingType, boolean hasChildren, int experienceWithPets, 
                  double monthlyIncome, List<String> lifestyle, String password) { 
        super(id, name, dateOfBirth, contactInfo);
        setHousingType(housingType);
        this.hasChildren = hasChildren;
        setExperienceWithPets(experienceWithPets);
        setMonthlyIncome(monthlyIncome);
        this.lifestyle = lifestyle != null ? lifestyle : new ArrayList<>();
        this.password = password;
    }
    
    public String getHousingType() { return housingType; }
    
    public void setHousingType(String housingType) {
        List<String> validTypes = Arrays.asList("apartment", "house", "condo");
        if (!validTypes.contains(housingType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid housing type");
        }
        this.housingType = housingType.toLowerCase();
    }
    
    public boolean hasChildren() { return hasChildren; }
    
    public void setHasChildren(boolean hasChildren) { this.hasChildren = hasChildren; }
    
    public int getExperienceWithPets() { return experienceWithPets; }
    
    public void setExperienceWithPets(int experienceWithPets) {
        if (experienceWithPets < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }
        this.experienceWithPets = experienceWithPets;
    }
    
    public double getMonthlyIncome() { return monthlyIncome; }
    
    public void setMonthlyIncome(double monthlyIncome) {
        if (monthlyIncome < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }
        this.monthlyIncome = monthlyIncome;
    }
    
    public List<String> getLifestyle() { return lifestyle; }
    
    public void setLifestyle(List<String> lifestyle) { 
        this.lifestyle = lifestyle != null ? lifestyle : new ArrayList<>(); 
    }
    
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    
    public void setMedicalRecord(MedicalRecord medicalRecord) { 
        this.medicalRecord = medicalRecord; 
    }
    
    public String getPassword() { return password; }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
    
    @Override
    public String getPersonType() {
        return "Adopter";
    }
    
    // Method overloading
    public void addLifestyle(String lifestyle) {
        this.lifestyle.add(lifestyle);
    }
    
    public void addLifestyle(List<String> lifestyles) {
        this.lifestyle.addAll(lifestyles);
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(
            ", Housing: %s, Children: %s, Experience: %d years, Income: $%.2f, Lifestyle: %s",
            housingType, hasChildren, experienceWithPets, monthlyIncome, lifestyle);
    }
}
