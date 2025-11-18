# Pet Adoption System

A Java desktop application for managing pet adoption workflows using Object-Oriented Programming (OOP) principles. The system tracks pets, adopters, foster homes, and matches pets with adopters based on compatibility.

## Table of Contents
1. [Project Overview](#project-overview)  
2. [Features](#features)  
3. [Modules](#modules)  
4. [Data Storage](#data-storage)  
5. [Folder Structure](#folder-structure)  
6. [Documentation](#documentation)   
7. [Contributing](#contributing)  
8. [License](#license)  
9. [Author](#author)  

## Project Overview
The Pet Adoption System is a console-based Java application that:  
- Manages pets, adopters, and foster homes.  
- Supports compatibility-based matching for adoptions.  
- Tracks adoption applications and logs actions.  
- Uses CSV files for persistent storage.  

The system is modular, making it easy to extend with a GUI, database integration, or web interface.

## Features
- Add, update, delete, and list pets, adopters, and foster homes.  
- Compatibility scoring to match pets with suitable adopters.  
- Application submission, review, and approval workflows.  
- Logging system to track all adoption decisions.  
- CSV-based storage for data persistence.  

## Modules
### Collections
- `AdoptionQueue.java` – Manages the queue of adoption applications.  
- `PetRegistry.java` – Stores and manages all registered pets.  

### Entities
- `Pet.java`, `Dog.java`, `Cat.java` – Pet classes with attributes.  
- `Adopter.java` – Represents adopters and their preferences.  
- `FosterHome.java` – Tracks foster homes and capacity.  
- `MedicalRecord.java` – Stores medical information for pets.  
- `Person.java` – Base class for human-related entities.  
- `AdoptionApplication.java` – Represents an adoption application.  

### System
- `AdoptionSystem.java` – Main application workflow and logic.  
- `CompatibilityCalculator.java` – Calculates compatibility between pets and adopters.  
- `CustomerService.java`, `CustomerSupport.java` – Handles user interactions.  
- `CustomExceptions.java` – User-defined exceptions for validation.  
- `FileHandler.java` – Handles CSV read/write operations.  

### Main
- `Main.java` – Entry point of the application.  

### Screenshots & Output

You can view all application outputs and screenshots in this PDF:
[Pet Adoption System Output Screenshots](docs/PetAdoptionSystem_Report.pdf)

## Data Storage

* `pets.csv` – Stores pet details.
* `adopters.csv` – Stores adopter details.
* `foster_homes.csv` – Stores foster home details.
* `applications.csv` – Stores adoption application details.
* `application_log.csv` – Tracks all actions on applications.

## Folder Structure

```
pet_adoption/
├── collections/
│   ├── AdoptionQueue.java
│   └── PetRegistry.java
├── entities/
│   ├── Adopter.java
│   ├── AdoptionApplication.java
│   ├── Cat.java
│   ├── Dog.java
│   ├── FosterHome.java
│   ├── MedicalRecord.java
│   ├── Person.java
│   └── Pet.java
├── system/
│   ├── AdoptionSystem.java
│   ├── CompatibilityCalculator.java
│   ├── CustomerService.java
│   ├── CustomerSupport.java
│   ├── CustomExceptions.java
│   └── FileHandler.java
├── docs/
│   ├── PetAdoptionSystem_Outputs.pdf
│   ├── USAGE_GUIDE.md
│   ├── LIMITATIONS.md
│   └── IMPROVEMENTS.md
├── Main.java
├── README.md
├── LICENSE
└── .gitignore

```

## Documentation

* [USAGE_GUIDE.md](docs/USAGE_GUIDE.md) – How to use the system.
* [LIMITATIONS.md](docs/LIMITATIONS.md) – Current limitations of the system.
* [IMPROVEMENTS.md](docs/IMPROVEMENTS.md) – Future enhancements and improvements.

## Contributing

* Fork the repo, make changes, and submit a pull request.
* Maintain code conventions and add new CSV files if needed.

## License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.

## Author

Avantikaa R

