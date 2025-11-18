package entities;

import java.time.LocalDate;

public abstract class Person {
    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private String contactInfo;
    
    public Person() {}
    
    public Person(String id, String name, LocalDate dateOfBirth, String contactInfo) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
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
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid date of birth");
        }
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getContactInfo() { return contactInfo; }
    
    public void setContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact info cannot be null or empty");
        }
        this.contactInfo = contactInfo;
    }
    
    public abstract String getPersonType();
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, DOB: %s, Contact: %s", 
                           id, name, dateOfBirth, contactInfo);
    }
}
