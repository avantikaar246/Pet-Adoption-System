import entities.*;
import system.AdoptionSystem;
import system.CustomExceptions;
import system.CustomerService;
import system.CustomerSupport;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static AdoptionSystem system = new AdoptionSystem();
    private static Scanner scanner = new Scanner(System.in);
    private static Adopter currentAdopter = null;

    private static final String ADMIN_PIN = "12345";

    public static void main(String[] args) {
        System.out.println("=== Welcome to Pet Adoption System ===");
        System.out.println("Data Storage: CSV Files");

        try {
            boolean exitApplication = false;

            while (!exitApplication) {
                String role = getRoleInput();

                if (role.equalsIgnoreCase("A") || role.equalsIgnoreCase("ADMIN") || role.equals("1")) {
                    if (checkAdminPin()) {
                        adminMenuLoop();
                    } else {
                        System.out.println("Returning to role selection...");
                    }
                } else if (role.equalsIgnoreCase("U") || role.equalsIgnoreCase("USER") || role.equals("2")) {
                    userMenuLoop();
                } else if (role.equalsIgnoreCase("EXIT") || role.equalsIgnoreCase("Q")) {
                    System.out.println("Exiting application...");
                    exitApplication = true;
                } else {
                    System.out.println("Invalid selection. Please choose '1' for Admin or '2' for User.");
                }

                if (!exitApplication) {
                    System.out.print("Do you want to quit the entire application? (yes/no): ");
                    String ans = scanner.nextLine().trim().toLowerCase();
                    if (ans.equals("yes") || ans.equals("y")) {
                        exitApplication = true;
                    }
                }
            }

            system.saveSystemData();
            System.out.println("Thank you for using Pet Adoption System!");
            System.out.println("All data has been saved to CSV files.");

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String getRoleInput() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Role Selection");
        System.out.println("=".repeat(50));
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.println("Type 'exit' or 'q' to quit.");
        System.out.print("Enter choice (1/2): ");
        return scanner.nextLine().trim();
    }

    private static boolean checkAdminPin() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter admin security number (PIN): ");
            String input = scanner.nextLine().trim();
            if (input.equals(ADMIN_PIN)) {
                System.out.println("Authentication successful. Welcome, Admin!");
                return true;
            } else {
                attempts++;
                System.out.println("Incorrect PIN. Attempts left: " + (3 - attempts));
            }
        }
        System.out.println("Too many incorrect attempts. Access denied.");
        return false;
    }

    private static void adminMenuLoop() {
        boolean back = false;
        while (!back) {
            displayAdminMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1: addSampleData(); break;
                case 2: registerNewPet(); break;
                case 3: registerNewFosterHome(); break;
                case 4: listAdopters(); break;
                case 5: listFosterHomes(); break;
                case 6: processAdoption(); break;
                case 7: manageFosterCare(); break;
                case 8: system.displaySystemStatus(); break;
                case 9: system.displayAdoptionQueue(); break;
                case 10: adminCustomerService(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option!");
            }
        }
    }

    private static void userMenuLoop() {
        boolean back = false;
        while (!back) {
            displayUserMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1: registerNewAdopter(); break;
                case 2: loginAsAdopter(); break;
                case 3: listAvailablePets(); break;
                case 4: submitAdoptionApplication(); break;
                case 5: showCompatibilityAnalysis(); break;
                case 6: userCustomerService(); break;
                case 7: returnAdoptedPet(); break;
                case 0: 
                    currentAdopter = null; // NEW: Logout adopter
                    back = true; 
                    break;
                default: System.out.println("Invalid option!");
            }
        }
    }

    //Admin menu
    private static void displayAdminMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               ADMIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1.  Add Sample Data");
        System.out.println("2.  Register New Pet");
        System.out.println("3.  Register New Foster Home");
        System.out.println("4.  List Registered Adopters");
        System.out.println("5.  List Foster Homes");
        System.out.println("6.  Process Next Adoption");
        System.out.println("7.  Manage Foster Care");
        System.out.println("8.  System Status");
        System.out.println("9.  View Adoption Queue");
        System.out.println("10. Customer Service (view/respond tickets)");
        System.out.println("0.  Logout (return to role selection)");
        System.out.println("-".repeat(50));
    }

    //User menu
    private static void displayUserMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               USER MENU");
        System.out.println("=".repeat(50));
        if (currentAdopter != null) {
            System.out.println("*** Logged in as: " + currentAdopter.getName() + " ***");
        }
        System.out.println("1.  Register as Adopter");
        System.out.println("2.  Login as Adopter");
        System.out.println("3.  List Available Pets");
        System.out.println("4.  Submit Adoption Application");
        System.out.println("5.  Check Compatibility Analysis");
        System.out.println("6.  Raise Ticket (Customer Service)");
        System.out.println("7.  Return Adopted Pet");
        System.out.println("0.  Logout (return to role selection)");
        System.out.println("-".repeat(50));
    }

    private static void loginAsAdopter() {
        System.out.println("\n=== ADOPTER LOGIN ===");
        
        System.out.print("Adopter ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        currentAdopter = system.loginAdopter(id, password);
        
        if (currentAdopter != null) {
            System.out.println("✓ Login successful! Welcome, " + currentAdopter.getName());
        } else {
            System.out.println("✗ Login failed. Invalid ID or password.");
        }
    }

    private static void adminCustomerService() {
        CustomerService service = system.getCustomerService();
        while (true) {
            System.out.println("\n=== CUSTOMER SERVICE (ADMIN) ===");
            System.out.println("1. View all tickets");
            System.out.println("2. Respond to a ticket");
            System.out.println("3. Back to Admin Menu");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    service.viewAllTickets();
                    break;
                case "2":
                    System.out.print("Enter Ticket ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Response: ");
                    String response = scanner.nextLine();
                    service.respondToTicket(id, response);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void userCustomerService() {
        if (currentAdopter == null) {
            System.out.println("Please login as adopter first to raise a ticket.");
            return;
        }
        
        CustomerService service = system.getCustomerService();
        System.out.println("\n=== RAISE CUSTOMER SERVICE TICKET ===");
        System.out.print("Describe your issue: ");
        String issue = scanner.nextLine();
        service.raiseTicket(currentAdopter.getId(), issue);
    }

    private static void registerNewAdopter() {
        System.out.println("\n=== REGISTER NEW ADOPTER ===");
        
        System.out.print("ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dobInput = scanner.nextLine();
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobInput);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }
        
        System.out.print("Contact Info: ");
        String contact = scanner.nextLine();
        
        System.out.print("Housing Type (apartment/house/condo): ");
        String housing = scanner.nextLine();
        
        System.out.print("Has Children? (true/false): ");
        boolean hasChildren = scanner.nextBoolean();
        scanner.nextLine();
        
        System.out.print("Years of Pet Experience: ");
        int experience = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Monthly Income: ");
        double income = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Lifestyle (comma separated - active,homebody,traveler): ");
        String[] lifestyleArray = scanner.nextLine().split(",");
        List<String> lifestyle = Arrays.asList(lifestyleArray);
        
	System.out.print("Password: ");
        String password = scanner.nextLine();

        Adopter adopter = new Adopter(id, name, dob, contact, housing, hasChildren, experience, income, lifestyle, password);
        system.registerAdopter(adopter);
        
        System.out.println("✓ Adopter registered successfully and saved to adopters.csv");
        
        System.out.println("✓ Please login with your ID and password to access user features.");
}

    private static void submitAdoptionApplication() {
        if (currentAdopter == null) {
            System.out.println("Please login as adopter first to submit an application.");
            return;
        }
        
        System.out.println("\n=== SUBMIT ADOPTION APPLICATION ===");
        System.out.println("Adopter: " + currentAdopter.getName() + " (" + currentAdopter.getId() + ")");
        
        System.out.print("Pet ID: ");
        String petId = scanner.nextLine();
        
        system.submitAdoptionApplication(currentAdopter.getId(), petId);
    }

    private static void showCompatibilityAnalysis() {
        if (currentAdopter == null) {
            System.out.println("Please login as adopter first to check compatibility.");
            return;
        }
        
        System.out.println("\n=== COMPATIBILITY ANALYSIS ===");
        System.out.println("Adopter: " + currentAdopter.getName() + " (" + currentAdopter.getId() + ")");
        
        System.out.print("Pet ID (or type 'ALL' for analysis with all available pets): ");
        String petId = scanner.nextLine();
        
        system.getCompatibilityAnalysis(currentAdopter.getId(), petId);
    }

    private static void returnAdoptedPet() {
        if (currentAdopter == null) {
            System.out.println("Please login as adopter first to return a pet.");
            return;
        }
        
        System.out.println("\n=== RETURN ADOPTED PET ===");
        
        System.out.print("Enter Pet ID to return: ");
        String petId = scanner.nextLine();
        
        System.out.print("Reason for return: ");
        String reason = scanner.nextLine();
        
        system.returnAdoptedPet(petId, reason);
    }

    
    private static void addSampleData() {
        System.out.println("\n=== ADD SAMPLE DATA ===");
        if (system.getAllPetsCount() > 0 || system.getAllAdoptersCount() > 0) {
            System.out.println("Existing data found. Sample data will be added only if no data exists.");
            System.out.print("Do you want to proceed? (yes/no): ");
            String response = scanner.nextLine().toLowerCase();
            if (!response.equals("yes") && !response.equals("y")) {
                System.out.println("Sample data addition cancelled.");
                return;
            }
        }
        
        System.out.println("Adding sample data...");

        if (system.getAllPetsCount() == 0) {
            MedicalRecord buddyMedical = new MedicalRecord("MED001");
            buddyMedical.setCurrentlySick(false);
            buddyMedical.addVaccination("Rabies");
            buddyMedical.addVaccination("Distemper");
            
            Dog buddy = new Dog("PET001", "Buddy", "Golden Retriever", 3, "male", "large", 8, true, true);
            buddy.setMedicalRecord(buddyMedical);
            system.addPet(buddy);
            
            MedicalRecord whiskersMedical = new MedicalRecord("MED002");
            whiskersMedical.setCurrentlySick(true);
            whiskersMedical.setCurrentCondition("Recovering from surgery");
            whiskersMedical.addVaccination("FVRCP");
            
            Cat whiskers = new Cat("PET002", "Whiskers", "Siamese", 2, "female",
                                  true, true, false, "playful");
            whiskers.setMedicalRecord(whiskersMedical);
            system.addPet(whiskers);
            
            System.out.println("✓ Sample pets added: Buddy (Dog) and Whiskers (Cat)");
        }

        if (system.getAllAdoptersCount() == 0) {
            Adopter john = new Adopter("ADPT001", "John Doe", LocalDate.of(1985, 5, 15), "555-0101", "house", true, 5, 5000.0, Arrays.asList("active", "homebody"), "JohnDoe01");
            system.registerAdopter(john);
            
            Adopter sarah = new Adopter("ADPT002", "Sarah Smith", LocalDate.of(1990, 8, 22), "555-0102", "apartment", false, 0, 3500.0, Arrays.asList("traveler", "active"), "SarahSmith02");
            system.registerAdopter(sarah);
            
            System.out.println("✓ Sample adopters added: John Doe and Sarah Smith");
        }
        
        if (system.getAllFosterHomesCount() == 0) {
            FosterHome home1 = new FosterHome("FOSTER001", "Happy Paws Foster", "555-0201", 5, new HashSet<>(Arrays.asList("sick", "special_needs")));
            system.addFosterHome(home1);
            System.out.println("✓ Sample foster home added: Happy Paws Foster");
        }
        
        System.out.println("\nSample data added successfully!");
        System.out.println("All data has been automatically saved to CSV files.");
    }
    
    private static void registerNewPet() {
        System.out.println("\n=== REGISTER NEW PET ===");
        
        System.out.print("Pet Type (1-Dog, 2-Cat): ");
        int type = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Breed: ");
        String breed = scanner.nextLine();
        
        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Gender (male/female): ");
        String gender = scanner.nextLine();
        
        Pet pet = null;
        
        if (type == 1) {
            System.out.print("Size (small/medium/large): ");
            String size = scanner.nextLine();
            
            System.out.print("Energy Level (1-10): ");
            int energy = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Good with children? (true/false): ");
            boolean goodWithKids = scanner.nextBoolean();
            scanner.nextLine();
            
            System.out.print("Requires yard? (true/false): ");
            boolean needsYard = scanner.nextBoolean();
            scanner.nextLine();
            
            pet = new Dog(id, name, breed, age, gender, size, energy, goodWithKids, needsYard);
            
        } else if (type == 2) {
            System.out.print("Indoor only? (true/false): ");
            boolean indoor = scanner.nextBoolean();
            scanner.nextLine();
            
            System.out.print("Gets along with other cats? (true/false): ");
            boolean withCats = scanner.nextBoolean();
            scanner.nextLine();
            
            System.out.print("Gets along with dogs? (true/false): ");
            boolean withDogs = scanner.nextBoolean();
            scanner.nextLine();
            
            System.out.print("Temperament (calm/playful/shy/independent): ");
            String temperament = scanner.nextLine();
            
            pet = new Cat(id, name, breed, age, gender, indoor, withCats, withDogs, temperament);
        } else {
            System.out.println("Invalid pet type selected.");
            return;
        }
        
        System.out.print("Does this pet need medical care/foster care? (yes/no): ");
        String needsFosterResponse = scanner.nextLine().toLowerCase();
        
        if (needsFosterResponse.equals("yes") || needsFosterResponse.equals("y")) {
            System.out.print("Medical condition (e.g., 'recovering from surgery', 'needs medication'): ");
            String condition = scanner.nextLine();
            
            MedicalRecord medicalRecord = new MedicalRecord("MED" + System.currentTimeMillis());
            medicalRecord.setCurrentlySick(true);
            medicalRecord.setCurrentCondition(condition);
            
            if (type == 1) { 
                medicalRecord.addVaccination("Rabies");
                medicalRecord.addVaccination("Distemper");
            } else {
                medicalRecord.addVaccination("FVRCP");
            }
            
            pet.setMedicalRecord(medicalRecord);
            System.out.println("✓ Medical record created. Pet will be assigned to foster care if needed.");
        } else {
            System.out.println("✓ Pet registered as healthy and available for immediate adoption.");
        }
        
        system.addPet(pet);
        System.out.println("✓ Pet registered successfully and saved to pets.csv");
    }
    
    private static void registerNewFosterHome() {
        System.out.println("\n=== REGISTER NEW FOSTER HOME ===");
        
        System.out.print("ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Contact Info: ");
        String contactInfo = scanner.nextLine();
        
        System.out.print("Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Specializations (comma separated - sick,elderly,special_needs): ");
        String[] specializationsArray = scanner.nextLine().split(",");
        Set<String> specializations = new HashSet<>(Arrays.asList(specializationsArray));
        
        FosterHome fosterHome = new FosterHome(id, name, contactInfo, capacity, specializations);
        system.addFosterHome(fosterHome);
        
        System.out.println("✓ Foster home registered successfully and saved to foster_homes.csv");
    }
    
    private static void listAvailablePets() {
        system.listAvailablePets();
    }
    
    private static void listAdopters() {
        system.listAdopters();
    }
    
    private static void listFosterHomes() {
        system.listFosterHomes();
    }
    
    private static void processAdoption() {
        System.out.println("\n=== PROCESS ADOPTION ===");
        System.out.println("Applications are processed by highest compatibility score first.");
        system.processNextAdoption();
    }
    
    private static void manageFosterCare() {
        System.out.println("\n=== MANAGE FOSTER CARE ===");
        System.out.println("1. List Pets in Foster Care");
        System.out.println("2. Un-foster Pet (Return to Available Pets)");
        
        int choice = getIntInput("Choose an option: ");
        
        switch (choice) {
            case 1:
                system.listPetsInFosterCare();
                break;
            case 2:
                unFosterPet();
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    
    private static void unFosterPet() {
        System.out.println("\n=== UN-FOSTER PET ===");
        
        system.listPetsInFosterCare();
        
        System.out.print("Enter Pet ID to un-foster: ");
        String petId = scanner.nextLine();
        
        system.unFosterPet(petId);
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            scanner.next();
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); 
        return input;
    }
}
