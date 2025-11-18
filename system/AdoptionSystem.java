package system;

import entities.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String PETS_CSV = "pets.csv";
    private static final String ADOPTERS_CSV = "adopters.csv";
    private static final String FOSTER_HOMES_CSV = "foster_homes.csv";
    private static final String APPLICATIONS_CSV = "applications.csv";
    private static final String APPLICATION_LOG_CSV = "application_log.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    private static final String PETS_HEADER = "ID,Name,Breed,Age,Gender,Type,Size,EnergyLevel,GoodWithChildren,RequiresYard,IsIndoor,GetsAlongWithCats,GetsAlongWithDogs,Temperament,Available,IntakeDate,MedicalRecordId,IsSick,SpecialNeeds,CurrentFosterHome";
    private static final String ADOPTERS_HEADER = "ID,Name,DateOfBirth,ContactInfo,HousingType,HasChildren,ExperienceWithPets,MonthlyIncome,Lifestyle,Password,MedicalRecordId";
    private static final String FOSTER_HOMES_HEADER = "ID,Name,ContactInfo,Capacity,CurrentOccupancy,Specializations";
    private static final String APPLICATIONS_HEADER = "ApplicationID,AdopterID,PetID,ApplicationDate,Status,CompatibilityScore,Notes";
    private static final String APPLICATION_LOG_HEADER = "Timestamp,Action,ApplicationID,AdopterName,AdopterID,PetName,PetID,CompatibilityScore,Status,Notes";
    
    public static void savePets(List<Pet> pets) throws CustomExceptions.FileOperationException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PETS_CSV))) {
            writer.println(PETS_HEADER);
            
            for (Pet pet : pets) {
                if (pet instanceof Dog) {
                    Dog dog = (Dog) pet;
                    writer.println(convertDogToCSV(dog));
                } else if (pet instanceof Cat) {
                    Cat cat = (Cat) pet;
                    writer.println(convertCatToCSV(cat));
                }
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to save pets to CSV", e);
        }
    }
    
    public static List<Pet> loadPets() throws CustomExceptions.FileOperationException {
        List<Pet> pets = new ArrayList<>();
        File file = new File(PETS_CSV);
        
        if (!file.exists()) {
            return pets;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(PETS_CSV))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                Pet pet = parsePetFromCSV(line);
                if (pet != null) {
                    pets.add(pet);
                }
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to load pets from CSV", e);
        }
        
        return pets;
    }
    
    private static String convertDogToCSV(Dog dog) {
        MedicalRecord medicalRecord = dog.getMedicalRecord();
        return String.join(",",
            escapeCsv(dog.getId()),
            escapeCsv(dog.getName()),
            escapeCsv(dog.getBreed()),
            String.valueOf(dog.getAge()),
            escapeCsv(dog.getGender()),
            "Dog",
            escapeCsv(dog.getSize()),
            String.valueOf(dog.getEnergyLevel()),
            String.valueOf(dog.isGoodWithChildren()),
            String.valueOf(dog.requiresYard()),
            "false", 
            "false", 
            "false",
            "",
            String.valueOf(dog.isAvailableForAdoption()),
            dog.getIntakeDate() != null ? dog.getIntakeDate().toString() : "",
            medicalRecord != null ? escapeCsv(medicalRecord.getRecordId()) : "",
            medicalRecord != null ? String.valueOf(medicalRecord.isCurrentlySick()) : "false",
            medicalRecord != null ? String.valueOf(medicalRecord.hasSpecialNeeds()) : "false",
            dog.getCurrentFosterHome() != null ? escapeCsv(dog.getCurrentFosterHome().getId()) : ""
        );
    }
    
    private static String convertCatToCSV(Cat cat) {
        MedicalRecord medicalRecord = cat.getMedicalRecord();
        return String.join(",",
            escapeCsv(cat.getId()),
            escapeCsv(cat.getName()),
            escapeCsv(cat.getBreed()),
            String.valueOf(cat.getAge()),
            escapeCsv(cat.getGender()),
            "Cat",
            "",
            "0",
            "false",
            "false",
            String.valueOf(cat.isIndoor()),
            String.valueOf(cat.getsAlongWithOtherCats()),
            String.valueOf(cat.getsAlongWithDogs()),
            escapeCsv(cat.getTemperament()),
            String.valueOf(cat.isAvailableForAdoption()),
            cat.getIntakeDate() != null ? cat.getIntakeDate().toString() : "",
            medicalRecord != null ? escapeCsv(medicalRecord.getRecordId()) : "",
            medicalRecord != null ? String.valueOf(medicalRecord.isCurrentlySick()) : "false",
            medicalRecord != null ? String.valueOf(medicalRecord.hasSpecialNeeds()) : "false",
            cat.getCurrentFosterHome() != null ? escapeCsv(cat.getCurrentFosterHome().getId()) : ""
        );
    }
    
    private static Pet parsePetFromCSV(String csvLine) {
        String[] fields = parseCsvLine(csvLine);
        if (fields.length < 10) return null;
        
        try {
            String type = fields[5];
            String id = unescapeCsv(fields[0]);
            String name = unescapeCsv(fields[1]);
            String breed = unescapeCsv(fields[2]);
            int age = Integer.parseInt(fields[3]);
            String gender = unescapeCsv(fields[4]);
            
            if ("Dog".equals(type)) {
                Dog dog = new Dog(id, name, breed, age, gender,
                    unescapeCsv(fields[6]),
                    Integer.parseInt(fields[7]), 
                    Boolean.parseBoolean(fields[8]),
                    Boolean.parseBoolean(fields[9])
                );
                dog.setAvailableForAdoption(Boolean.parseBoolean(fields[14]));
                return dog;
            } else if ("Cat".equals(type)) {
                Cat cat = new Cat(id, name, breed, age, gender,
                    Boolean.parseBoolean(fields[10]),
                    Boolean.parseBoolean(fields[11]),
                    Boolean.parseBoolean(fields[12]),
                    unescapeCsv(fields[13])
                );
                cat.setAvailableForAdoption(Boolean.parseBoolean(fields[14]));
                return cat;
            }
        } catch (Exception e) {
            System.err.println("Error parsing pet from CSV: " + e.getMessage());
        }
        
        return null;
    }
    
    public static void saveAdopters(List<Adopter> adopters) throws CustomExceptions.FileOperationException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ADOPTERS_CSV))) {
            writer.println(ADOPTERS_HEADER);
            
            for (Adopter adopter : adopters) {
                writer.println(convertAdopterToCSV(adopter));
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to save adopters to CSV", e);
        }
    }
    
    public static List<Adopter> loadAdopters() throws CustomExceptions.FileOperationException {
        List<Adopter> adopters = new ArrayList<>();
        File file = new File(ADOPTERS_CSV);
        
        if (!file.exists()) {
            return adopters;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ADOPTERS_CSV))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                Adopter adopter = parseAdopterFromCSV(line);
                if (adopter != null) {
                    adopters.add(adopter);
                }
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to load adopters from CSV", e);
        }
        
        return adopters;
    }
    
    private static String convertAdopterToCSV(Adopter adopter) {
        List<String> lifestyle = adopter.getLifestyle();
        String lifestyleStr = lifestyle != null ? String.join(";", lifestyle) : "";
        
        return String.join(",",
            escapeCsv(adopter.getId()),
            escapeCsv(adopter.getName()),
            adopter.getDateOfBirth() != null ? adopter.getDateOfBirth().toString() : "",
            escapeCsv(adopter.getContactInfo()),
            escapeCsv(adopter.getHousingType()),
            String.valueOf(adopter.hasChildren()),
            String.valueOf(adopter.getExperienceWithPets()),
            String.valueOf(adopter.getMonthlyIncome()),
            escapeCsv(lifestyleStr),
            escapeCsv(adopter.getPassword() != null ? adopter.getPassword() : "default"),
            adopter.getMedicalRecord() != null ? escapeCsv(adopter.getMedicalRecord().getRecordId()) : ""
        );
    }
    
    private static Adopter parseAdopterFromCSV(String csvLine) {
        String[] fields = parseCsvLine(csvLine);
        if (fields.length < 10) return null;
        
        try {
            String id = unescapeCsv(fields[0]);
            String name = unescapeCsv(fields[1]);
            LocalDate dob = fields[2].isEmpty() ? null : LocalDate.parse(fields[2]);
            String contact = unescapeCsv(fields[3]);
            String housing = unescapeCsv(fields[4]);
            boolean hasChildren = Boolean.parseBoolean(fields[5]);
            int experience = Integer.parseInt(fields[6]);
            double income = Double.parseDouble(fields[7]);
            
            List<String> lifestyle = new ArrayList<>();
            if (!fields[8].isEmpty()) {
                String[] lifestyleArray = unescapeCsv(fields[8]).split(";");
                lifestyle = Arrays.asList(lifestyleArray);
            }
            
            String password = "default";
            if (fields.length > 9 && !fields[9].isEmpty()) {
                password = unescapeCsv(fields[9]);
            }
            
            return new Adopter(id, name, dob, contact, housing, hasChildren, experience, income, lifestyle, password);
        } catch (Exception e) {
            System.err.println("Error parsing adopter from CSV: " + e.getMessage());
        }
        
        return null;
    }
    

    public static void saveFosterHomes(List<FosterHome> fosterHomes) throws CustomExceptions.FileOperationException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FOSTER_HOMES_CSV))) {
            writer.println(FOSTER_HOMES_HEADER);
            
            for (FosterHome home : fosterHomes) {
                writer.println(convertFosterHomeToCSV(home));
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to save foster homes to CSV", e);
        }
    }
    
    public static List<FosterHome> loadFosterHomes() throws CustomExceptions.FileOperationException {
        List<FosterHome> fosterHomes = new ArrayList<>();
        File file = new File(FOSTER_HOMES_CSV);
        
        if (!file.exists()) {
            return fosterHomes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FOSTER_HOMES_CSV))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                FosterHome home = parseFosterHomeFromCSV(line);
                if (home != null) {
                    fosterHomes.add(home);
                }
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to load foster homes from CSV", e);
        }
        
        return fosterHomes;
    }
    
    private static String convertFosterHomeToCSV(FosterHome home) {
        Set<String> specializations = home.getSpecializations();
        String specsStr = specializations != null ? String.join(";", specializations) : "";
        
        return String.join(",",
            escapeCsv(home.getId()),
            escapeCsv(home.getName()),
            escapeCsv(home.getContactInfo()),
            String.valueOf(home.getCapacity()),
            String.valueOf(home.getCurrentOccupancy()),
            escapeCsv(specsStr)
        );
    }
    
    private static FosterHome parseFosterHomeFromCSV(String csvLine) {
        String[] fields = parseCsvLine(csvLine);
        if (fields.length < 6) return null;
        
        try {
            String id = unescapeCsv(fields[0]);
            String name = unescapeCsv(fields[1]);
            String contact = unescapeCsv(fields[2]);
            int capacity = Integer.parseInt(fields[3]);
            
            Set<String> specializations = new HashSet<>();
            if (!fields[5].isEmpty()) {
                String[] specsArray = unescapeCsv(fields[5]).split(";");
                specializations.addAll(Arrays.asList(specsArray));
            }
            
            FosterHome home = new FosterHome(id, name, contact, capacity, specializations);
            home.setCurrentOccupancy(Integer.parseInt(fields[4]));
            return home;
        } catch (Exception e) {
            System.err.println("Error parsing foster home from CSV: " + e.getMessage());
        }
        
        return null;
    }
    
    public static void saveApplications(List<AdoptionApplication> applications) throws CustomExceptions.FileOperationException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPLICATIONS_CSV))) {
            writer.println(APPLICATIONS_HEADER);
            
            for (AdoptionApplication app : applications) {
                writer.println(convertApplicationToCSV(app));
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to save applications to CSV", e);
        }
    }
    
    public static List<AdoptionApplication> loadApplications() throws CustomExceptions.FileOperationException {
        List<AdoptionApplication> applications = new ArrayList<>();
        File file = new File(APPLICATIONS_CSV);
        
        if (!file.exists()) {
            return applications;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATIONS_CSV))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                AdoptionApplication app = parseApplicationFromCSV(line);
                if (app != null) {
                    applications.add(app);
                }
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to load applications from CSV", e);
        }
        
        return applications;
    }
    
    private static String convertApplicationToCSV(AdoptionApplication app) {
        return String.join(",",
            escapeCsv(app.getApplicationId()),
            escapeCsv(app.getAdopter().getId()),
            escapeCsv(app.getPet().getId()),
            app.getApplicationDate().toString(),
            escapeCsv(app.getStatus()),
            String.valueOf(app.getCompatibilityScore()),
            escapeCsv(app.getNotes() != null ? app.getNotes() : "")
        );
    }
    
    private static AdoptionApplication parseApplicationFromCSV(String csvLine) {
        String[] fields = parseCsvLine(csvLine);
        if (fields.length < 6) return null;
        
        try {
            String appId = unescapeCsv(fields[0]);
            String adopterId = unescapeCsv(fields[1]);
            String petId = unescapeCsv(fields[2]);
            LocalDate appDate = LocalDate.parse(fields[3]);
            String status = unescapeCsv(fields[4]);
            double score = Double.parseDouble(fields[5]);
            String notes = fields.length > 6 ? unescapeCsv(fields[6]) : null;
            
            // Note: Adopter and Pet objects need to be linked from main system
            // This will be handled in AdoptionSystem when loading
            AdoptionApplication app = new AdoptionApplication(appId, null, null);
            app.setApplicationDate(appDate);
            app.setStatus(status);
            app.setCompatibilityScore(score);
            app.setNotes(notes);
            
            return app;
        } catch (Exception e) {
            System.err.println("Error parsing application from CSV: " + e.getMessage());
        }
        
        return null;
    }
    
    public static void logApplication(AdoptionApplication application, String action) 
            throws CustomExceptions.FileOperationException {
        File logFile = new File(APPLICATION_LOG_CSV);
        boolean fileExists = logFile.exists();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPLICATION_LOG_CSV, true))) {
            if (!fileExists) {
                writer.println(APPLICATION_LOG_HEADER);
            }
            
            writer.println(convertApplicationLogToCSV(application, action));
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to log application", e);
        }
    }
    
    public static void displayApplicationLog() throws CustomExceptions.FileOperationException {
        File logFile = new File(APPLICATION_LOG_CSV);
        if (!logFile.exists()) {
            System.out.println("No application log found.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_LOG_CSV))) {
            String line;
            boolean isHeader = true;
            System.out.println("\n=== APPLICATION HISTORY LOG ===");
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                System.out.println(formatApplicationLogForDisplay(line));
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to read application log", e);
        }
    }
    
    private static String convertApplicationLogToCSV(AdoptionApplication app, String action) {
        return String.join(",",
            LocalDate.now() + " " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")),
            escapeCsv(action),
            escapeCsv(app.getApplicationId()),
            escapeCsv(app.getAdopter().getName()),
            escapeCsv(app.getAdopter().getId()),
            escapeCsv(app.getPet().getName()),
            escapeCsv(app.getPet().getId()),
            String.valueOf(app.getCompatibilityScore()),
            escapeCsv(app.getStatus()),
            escapeCsv(app.getNotes() != null ? app.getNotes() : "")
        );
    }
    
    private static String formatApplicationLogForDisplay(String csvLine) {
        String[] fields = parseCsvLine(csvLine);
        if (fields.length < 9) return csvLine;
        
        String action = unescapeCsv(fields[1]);
        
        if ("PET_RETURN".equals(action)) {
            return String.format("[%s] %s - %s was returned | Reason: %s",
                unescapeCsv(fields[0]),
                "PET RETURN",
                unescapeCsv(fields[5]),
                unescapeCsv(fields[9]).replace("Return reason: ", "")
            );
        } else {
            return String.format("[%s] %s - %s applied for %s | Score: %.1f%% | Status: %s",
                unescapeCsv(fields[0]),
                unescapeCsv(fields[1]).toUpperCase(),
                unescapeCsv(fields[3]),
                unescapeCsv(fields[5]),
                Double.parseDouble(fields[7]),
                unescapeCsv(fields[8])
            );
        }
    }

    public static void logPetReturn(Pet pet, String reason) 
            throws CustomExceptions.FileOperationException {
        File logFile = new File(APPLICATION_LOG_CSV);
        boolean fileExists = logFile.exists();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPLICATION_LOG_CSV, true))) {
            if (!fileExists) {
                writer.println(APPLICATION_LOG_HEADER);
            }
        
            String timestamp = LocalDate.now() + " " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            String logEntry = String.join(",",
                escapeCsv(timestamp),
                "PET_RETURN",
                "", 
                "",
                "",
                escapeCsv(pet.getName()),
                escapeCsv(pet.getId()),
                "0",
                "RETURNED",
                escapeCsv("Return reason: " + reason)
            );
        
            writer.println(logEntry);
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to log pet return", e);
        }
    }

    public static void exportApprovedApplicationsReport(List<AdoptionApplication> applications) 
            throws CustomExceptions.FileOperationException {
        String filename = "approved_applications_report_" + LocalDate.now() + ".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ApplicationID,AdopterName,PetName,CompatibilityScore,ApplicationDate,Status");
            
            for (AdoptionApplication app : applications) {
                if ("approved".equals(app.getStatus())) {
                    writer.println(String.join(",",
                        escapeCsv(app.getApplicationId()),
                        escapeCsv(app.getAdopter().getName()),
                        escapeCsv(app.getPet().getName()),
                        String.valueOf(app.getCompatibilityScore()),
                        app.getApplicationDate().toString(),
                        escapeCsv(app.getStatus())
                    ));
                }
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileOperationException("Failed to export report", e);
        }
    }
    
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private static String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\"\"", "\"");
    }
    
    private static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
}
