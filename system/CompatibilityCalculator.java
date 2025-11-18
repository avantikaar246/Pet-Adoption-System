package system;

import entities.*;
import java.util.*;

public class CompatibilityCalculator {
    
    public double calculateCompatibility(Adopter adopter, Pet pet) {
        double totalScore = 0.0;
        int factorCount = 0;
        
        double housingScore = calculateHousingCompatibility(adopter, pet);
        totalScore += housingScore * 0.25;
        factorCount++;
        
        double lifestyleScore = calculateLifestyleCompatibility(adopter, pet);
        totalScore += lifestyleScore * 0.25;
        factorCount++;
        
        double experienceScore = calculateExperienceCompatibility(adopter, pet);
        totalScore += experienceScore * 0.20;
        factorCount++;
        
        double financialScore = calculateFinancialCompatibility(adopter, pet);
        totalScore += financialScore * 0.15;
        factorCount++;
        
        double familyScore = calculateFamilyCompatibility(adopter, pet);
        totalScore += familyScore * 0.15;
        factorCount++;
        
        double finalScore = Math.min(100.0, totalScore * 100);
        
        if (housingScore >= 0.8 && lifestyleScore >= 0.8 && experienceScore >= 0.8 && 
            financialScore >= 0.8 && familyScore >= 0.8) {
            return Math.max(85.0, finalScore);
        }
        
        return finalScore;
    }
    
    private double calculateHousingCompatibility(Adopter adopter, Pet pet) {
        double score = 0.0;
        
        if (pet instanceof Dog) {
            Dog dog = (Dog) pet;
            
            if (dog.requiresYard() && adopter.getHousingType().equals("house")) {
                score = 1.0;
            } else if (dog.requiresYard() && !adopter.getHousingType().equals("house")) {
                score = 0.3;
            } else {
                switch (adopter.getHousingType()) {
                    case "house": score = 1.0; break;
                    case "condo": score = 0.8; break;
                    case "apartment": score = 0.6; break;
                }
            }
            
        } else if (pet instanceof Cat) {
            Cat cat = (Cat) pet;
            if (cat.isIndoor()) {
                switch (adopter.getHousingType()) {
                    case "house": score = 1.0; break;
                    case "condo": score = 0.9; break;
                    case "apartment": score = 0.8; break;
                }
            } else {
                if (adopter.getHousingType().equals("house")) {
                    score = 1.0;
                } else {
                    score = 0.4;
                }
            }
        }
        
        return score;
    }
    
    private double calculateLifestyleCompatibility(Adopter adopter, Pet pet) {
        double score = 0.0;
        boolean isTraveler = adopter.getLifestyle().contains("traveler");
        boolean isSickPet = pet.getMedicalRecord() != null && pet.getMedicalRecord().isCurrentlySick();
        
        if (pet instanceof Dog) {
            Dog dog = (Dog) pet;
            int petEnergy = dog.getEnergyLevel();
            
            if (isTraveler) {
                score = (petEnergy <= 3) ? 0.7 : 0.3;
            } else if (adopter.getLifestyle().contains("active")) {
                score += (petEnergy >= 7) ? 1.0 : 0.7;
            } else if (adopter.getLifestyle().contains("homebody")) {
                score += (petEnergy <= 4) ? 1.0 : 0.5;
            }
            
        } else if (pet instanceof Cat) {
            Cat cat = (Cat) pet;
            String temperament = cat.getTemperament();
            
            if (isTraveler) {
                if (isSickPet) {
                    score = 0.1;
                } else if (temperament.equals("independent")) {
                    score = 0.7;
                } else {
                    score = 0.3;
                }
            } else if (adopter.getLifestyle().contains("active")) {
                score += temperament.equals("playful") ? 1.0 : 0.6;
            } else if (adopter.getLifestyle().contains("homebody")) {
                score += (temperament.equals("calm") || temperament.equals("shy")) ? 1.0 : 0.7;
            }
        }
        
        return Math.min(1.0, score);
    }
    
    private double calculateExperienceCompatibility(Adopter adopter, Pet pet) {
        int experience = adopter.getExperienceWithPets();
        double score = 0.0;
        
        boolean isSickPet = pet.getMedicalRecord() != null && pet.getMedicalRecord().isCurrentlySick();
        boolean hasSpecialNeeds = pet.getMedicalRecord() != null && pet.getMedicalRecord().hasSpecialNeeds();
        
        if (hasSpecialNeeds || isSickPet) {
            if (isSickPet && experience == 0) {
                score = 0.1; 
            } else if (hasSpecialNeeds) {
                score = experience >= 3 ? 1.0 : experience >= 1 ? 0.6 : 0.2;
            } else if (isSickPet) {
                score = experience >= 2 ? 0.8 : experience >= 1 ? 0.4 : 0.1;
            }
        } else if (pet instanceof Dog && ((Dog) pet).getEnergyLevel() >= 7) {
            score = experience >= 2 ? 1.0 : experience >= 1 ? 0.7 : 0.4;
        } else {
            score = experience >= 1 ? 1.0 : 0.8;
        }
        
        return score;
    }
    
    private double calculateFinancialCompatibility(Adopter adopter, Pet pet) {
        double monthlyCost = pet.getDailyCareCost() * 30;
        double income = adopter.getMonthlyIncome();
        
        double costRatio = monthlyCost / income;
        
        if (costRatio <= 0.05) return 1.0;
        else if (costRatio <= 0.10) return 0.8;
        else if (costRatio <= 0.15) return 0.6;
        else return 0.3;
    }
    
    private double calculateFamilyCompatibility(Adopter adopter, Pet pet) {
        double score = 1.0;
        
        if (adopter.hasChildren()) {
            if (pet instanceof Dog) {
                Dog dog = (Dog) pet;
                score = dog.isGoodWithChildren() ? 1.0 : 0.2;
            } else if (pet instanceof Cat) {
                Cat cat = (Cat) pet;
                score = cat.getTemperament().equals("shy") ? 0.5 : 0.8;
            }
        }
        
        return score;
    }
    
    public String getCompatibilityAnalysis(Adopter adopter, Pet pet) {
        double score = calculateCompatibility(adopter, pet);
        StringBuilder analysis = new StringBuilder();
        
        double housing = calculateHousingCompatibility(adopter, pet);
        double lifestyle = calculateLifestyleCompatibility(adopter, pet);
        double experience = calculateExperienceCompatibility(adopter, pet);
        double financial = calculateFinancialCompatibility(adopter, pet);
        double family = calculateFamilyCompatibility(adopter, pet);
        
        analysis.append(String.format("Overall Compatibility: %.1f%%\n", score));
        analysis.append("Breakdown:\n");
        analysis.append(String.format("- Housing: %.1f%%\n", housing * 100));
        analysis.append(String.format("- Lifestyle: %.1f%%\n", lifestyle * 100));
        analysis.append(String.format("- Experience: %.1f%%\n", experience * 100));
        analysis.append(String.format("- Financial: %.1f%%\n", financial * 100));
        analysis.append(String.format("- Family: %.1f%%\n", family * 100));
        
        boolean perfectMatch = housing >= 0.8 && lifestyle >= 0.8 && 
                              experience >= 0.8 && financial >= 0.8 && family >= 0.8;
        
        if (perfectMatch) {
            analysis.append("\n>>> PERFECT MATCH! All criteria exceed 80%\n");
        }
        
        analysis.append("\nDetailed Analysis:\n");
        
        String housingType = adopter.getHousingType();
        if (pet instanceof Dog) {
            Dog dog = (Dog) pet;
            if (dog.requiresYard() && !housingType.equals("house")) {
                analysis.append("- Housing: Yard required but not available in " + housingType + "\n");
            } else if (!dog.requiresYard()) {
                analysis.append("- Housing: Suitable for " + housingType + " living\n");
            }
        } else if (pet instanceof Cat) {
            Cat cat = (Cat) pet;
            if (cat.isIndoor()) {
                analysis.append("- Housing: Indoor cat suitable for any housing\n");
            } else if (!housingType.equals("house")) {
                analysis.append("- Housing: Outdoor cat may need safer environment\n");
            }
        }
        
        List<String> lifestyleList = adopter.getLifestyle();
        boolean isTraveler = lifestyleList.contains("traveler");
        boolean isSickPet = pet.getMedicalRecord() != null && pet.getMedicalRecord().isCurrentlySick();
        
        if (isTraveler && isSickPet) {
            analysis.append("- Lifestyle: 🚨 CRITICAL - Traveler cannot provide constant care for sick pet\n");
        } else if (isTraveler) {
            analysis.append("- Lifestyle: Frequent travel may affect pet care routine\n");
        }
        
        if (pet instanceof Dog) {
            Dog dog = (Dog) pet;
            if (lifestyleList.contains("traveler") && dog.getEnergyLevel() > 3) {
                analysis.append("- Lifestyle: High-energy dog may not suit frequent traveler\n");
            } else if (lifestyleList.contains("active") && dog.getEnergyLevel() >= 7) {
                analysis.append("- Lifestyle: Active lifestyle matches high-energy dog\n");
            }
        } else if (pet instanceof Cat) {
            Cat cat = (Cat) pet;
            if (lifestyleList.contains("traveler") && !cat.getTemperament().equals("independent")) {
                analysis.append("- Lifestyle: Cat may need more attention than traveler can provide\n");
            }
        }
        
        int experienceYears = adopter.getExperienceWithPets();
        if (experienceYears == 0 && isSickPet) {
            analysis.append("- Experience: 🚨 CRITICAL - First-time owner cannot handle sick pet\n");
        } else if (experienceYears == 0) {
            analysis.append("- Experience: First-time pet owner - consider beginner-friendly pets\n");
        } else if (experienceYears >= 3) {
            analysis.append("- Experience: Experienced owner - can handle various pet needs\n");
        }
        
        double monthlyCost = pet.getDailyCareCost() * 30;
        double incomeRatio = (monthlyCost / adopter.getMonthlyIncome()) * 100;
        analysis.append(String.format("- Financial: Pet costs represent %.1f%% of monthly income\n", incomeRatio));
        
        if (adopter.hasChildren()) {
            if (pet instanceof Dog) {
                Dog dog = (Dog) pet;
                if (dog.isGoodWithChildren()) {
                    analysis.append("- Family: Dog is good with children - family friendly\n");
                } else {
                    analysis.append("- Family: Dog may not be suitable for households with children\n");
                }
            } else if (pet instanceof Cat) {
                Cat cat = (Cat) pet;
                if (cat.getTemperament().equals("shy")) {
                    analysis.append("- Family: Shy cat may need quiet environment without children\n");
                } else {
                    analysis.append("- Family: Cat should be fine with children\n");
                }
            }
        }
        
        analysis.append("\nRecommendations:\n");
        if (score >= 80 || perfectMatch) {
            analysis.append(">>> EXCELLENT MATCH! Strong recommendation for adoption.\n");
            analysis.append("    This pairing shows high compatibility across all factors.\n");
        } else if (score >= 60) {
            analysis.append(">>> GOOD MATCH. Consider adoption with proper preparation.\n");
            analysis.append("    Some areas may need attention but overall good potential.\n");
        } else if (score >= 40) {
            analysis.append(">>> MODERATE MATCH. Requires significant adjustments.\n");
            analysis.append("    Consider if you can accommodate the pet's specific needs.\n");
        } else {
            analysis.append(">>> POOR MATCH. Not recommended for adoption.\n");
            analysis.append("    Significant incompatibilities that may affect pet's well-being.\n");
        }
        
        return analysis.toString();
    }
    
    public String getQuickCompatibilitySummary(Adopter adopter, Pet pet) {
        double score = calculateCompatibility(adopter, pet);
        String status;
        
        if (score >= 80) status = "EXCELLENT";
        else if (score >= 60) status = "GOOD"; 
        else if (score >= 40) status = "MODERATE";
        else status = "POOR";
        
        return String.format("%s (%.1f%%)", status, score);
    }
}
