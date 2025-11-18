package entities;

import java.util.*;

public class Cat extends Pet {
    private boolean isIndoor;
    private boolean getsAlongWithOtherCats;
    private boolean getsAlongWithDogs;
    private String temperament; // "calm", "playful", "shy", "independent"
    
    public Cat() {
        super();
    }
    
    public Cat(String id, String name, String breed, int age, String gender,
               boolean isIndoor, boolean getsAlongWithOtherCats, 
               boolean getsAlongWithDogs, String temperament) {
        super(id, name, breed, age, gender);
        this.isIndoor = isIndoor;
        this.getsAlongWithOtherCats = getsAlongWithOtherCats;
        this.getsAlongWithDogs = getsAlongWithDogs;
        setTemperament(temperament);
    }
    
    public boolean isIndoor() { return isIndoor; }
    
    public void setIndoor(boolean indoor) { this.isIndoor = indoor; }
    
    public boolean getsAlongWithOtherCats() { return getsAlongWithOtherCats; }
    
    public void setGetsAlongWithOtherCats(boolean getsAlongWithOtherCats) { 
        this.getsAlongWithOtherCats = getsAlongWithOtherCats; 
    }
    
    public boolean getsAlongWithDogs() { return getsAlongWithDogs; }
    
    public void setGetsAlongWithDogs(boolean getsAlongWithDogs) { 
        this.getsAlongWithDogs = getsAlongWithDogs; 
    }
    
    public String getTemperament() { return temperament; }
    
    public void setTemperament(String temperament) {
        List<String> validTemperaments = Arrays.asList("calm", "playful", "shy", "independent");
        if (!validTemperaments.contains(temperament.toLowerCase())) {
            throw new IllegalArgumentException("Invalid temperament");
        }
        this.temperament = temperament.toLowerCase();
    }
    
    @Override
    public String getPetType() {
        return "Cat";
    }
    
    @Override
    public double getDailyCareCost() {
        return 10.0; 
    }
    
    @Override
    public int getSpaceRequirement() {
        return 200;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(
            ", Type: Cat, Indoor: %s, Gets with cats: %s, Gets with dogs: %s, Temperament: %s",
            isIndoor, getsAlongWithOtherCats, getsAlongWithDogs, temperament);
    }
}
