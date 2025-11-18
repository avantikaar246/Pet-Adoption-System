package entities;

import java.util.*;

public class Dog extends Pet {
    private String size; // "small", "medium", "large"
    private int energyLevel; // 1-10
    private boolean isGoodWithChildren;
    private boolean requiresYard;
    
    public Dog() {
        super();
    }
    
    public Dog(String id, String name, String breed, int age, String gender,
               String size, int energyLevel, boolean isGoodWithChildren, boolean requiresYard) {
        super(id, name, breed, age, gender);
        setSize(size);
        setEnergyLevel(energyLevel);
        this.isGoodWithChildren = isGoodWithChildren;
        this.requiresYard = requiresYard;
    }
    
    public String getSize() { return size; }
    
    public void setSize(String size) {
        List<String> validSizes = Arrays.asList("small", "medium", "large");
        if (!validSizes.contains(size.toLowerCase())) {
            throw new IllegalArgumentException("Invalid size");
        }
        this.size = size.toLowerCase();
    }
    
    public int getEnergyLevel() { return energyLevel; }
    
    public void setEnergyLevel(int energyLevel) {
        if (energyLevel < 1 || energyLevel > 10) {
            throw new IllegalArgumentException("Energy level must be between 1-10");
        }
        this.energyLevel = energyLevel;
    }
    
    public boolean isGoodWithChildren() { return isGoodWithChildren; }
    
    public void setGoodWithChildren(boolean goodWithChildren) { 
        this.isGoodWithChildren = goodWithChildren; 
    }
    
    public boolean requiresYard() { return requiresYard; }
    
    public void setRequiresYard(boolean requiresYard) { 
        this.requiresYard = requiresYard; 
    }
    
    @Override
    public String getPetType() {
        return "Dog";
    }
    
    @Override
    public double getDailyCareCost() {
        switch (size) {
            case "small": return 15.0;
            case "medium": return 25.0;
            case "large": return 35.0;
            default: return 20.0;
        }
    }
    
    @Override
    public int getSpaceRequirement() {
        switch (size) {
            case "small": return 300;
            case "medium": return 600;
            case "large": return 1000;
            default: return 500;
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(
            ", Type: Dog, Size: %s, Energy: %d/10, Good with kids: %s, Needs yard: %s",
            size, energyLevel, isGoodWithChildren, requiresYard);
    }
}
