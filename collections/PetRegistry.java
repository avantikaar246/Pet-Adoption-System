package collections;

import entities.Pet;
import java.util.*;
import java.util.stream.Collectors;

public class PetRegistry<T extends Pet> {
    private List<T> pets;
    
    public PetRegistry() {
        this.pets = new ArrayList<>();
    }
    
    public void addPet(T pet) {
        pets.add(pet);
    }
    
    public boolean removePet(String petId) {
        return pets.removeIf(pet -> pet.getId().equals(petId));
    }
    
    public T findPetById(String petId) {
        return pets.stream()
                  .filter(pet -> pet.getId().equals(petId))
                  .findFirst()
                  .orElse(null);
    }
    
    public List<T> findPetsByType(Class<?> petType) {
        return pets.stream()
                  .filter(pet -> petType.isInstance(pet))
                  .collect(Collectors.toList());
    }
    
    public List<T> getAvailablePets() {
        return pets.stream()
                  .filter(Pet::isAvailableForAdoption)
                  .collect(Collectors.toList());
    }
    
    public List<T> getPetsNeedingFosterCare() {
        return pets.stream()
                  .filter(pet -> pet.needsFosterCare() && pet.getCurrentFosterHome() == null)
                  .collect(Collectors.toList());
    }

    public List<T> getAllPetsThatNeedFosterCare() {
        return pets.stream()
                  .filter(Pet::needsFosterCare)
                  .collect(Collectors.toList());
    }
    
    public <U extends T> void addAllPets(List<U> newPets) {
        pets.addAll(newPets);
    }
    
    public void sortPets(Comparator<? super T> comparator) {
        pets.sort(comparator);
    }
    
    public List<T> getAllPets() {
        return new ArrayList<>(pets);
    }
    
    public int size() {
        return pets.size();
    }
}
