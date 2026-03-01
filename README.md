# Auto Insurance Management System 🚗💼

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17%2B-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

A comprehensive **Auto Insurance Management System** built with **JavaFX** for the front-end and **MySQL** for the back-end. This system allows insurance companies to manage customers, vehicles, policies, accidents, claims, installments, and branches efficiently.

---

## 📋 Table of Contents
- [Overview](#-overview)
- [Features](#-features)
- [Technologies Used](#-technologies-used)
- [System Architecture](#-system-architecture)
- [ERD Diagram](#-erd-diagram)
- [Screenshots](#-screenshots)
- [Installation](#-installation)
- [How to Run](#-how-to-run)
- [Project Structure](#-project-structure)
- [Contributors](#-contributors)
- [License](#-license)

---

## 📌 Overview
This project was developed as part of the **Database Course (COMP333)** at the university. The goal was to design and implement a fully functional database system for an auto insurance company, complete with a graphical user interface (GUI) for easy interaction.

The system supports:
- Managing branches, customers, addresses, and vehicles.
- Issuing and tracking insurance policies.
- Recording accidents and linking them to drivers and vehicles.
- Filing and managing claims.
- Tracking installment payments.

---

## ✨ Features
✅ **Branch Management** – Add, update, and view company branches.  
✅ **Customer Management** – Register new customers with personal details and multiple addresses.  
✅ **Vehicle Management** – Register vehicles and link them to owners.  
✅ **Driver Management** – Store driver information and link drivers to vehicles.  
✅ **Policy Management** – Create and manage insurance policies.  
✅ **Accident Recording** – Log accidents, involved drivers, and vehicles.  
✅ **Claims Processing** – File and track claims related to accidents.  
✅ **Installment Tracking** – Monitor policy payments and due dates.  
✅ **Search & Filter** – Quickly find records using various criteria.  
✅ **User-Friendly Interface** – Built with JavaFX for a smooth desktop experience.

---

## 🛠 Technologies Used
| Technology | Purpose |
|------------|---------|
| **Java 17** | Core programming language |
| **JavaFX 17** | Desktop GUI framework |
| **MySQL 8.0** | Relational database management system |
| **JDBC** | Database connectivity |
| **Scene Builder** | FXML design for UI |
| **Git & GitHub** | Version control and collaboration |
| **Mermaid / Draw.io** | ERD diagram design |

---

## 🏗 System Architecture
The system follows the **MVC (Model-View-Controller)** architecture pattern:
- **Model**: Java classes representing database entities (e.g., `Customer.java`, `Vehicle.java`, `Accident.java`).
- **View**: FXML files defining the user interface (e.g., `Customer.fxml`, `AccidentList.fxml`).
- **Controller**: Java classes handling user actions and business logic (e.g., `CustomerController.java`, `AccidentController.java`).

---

## 📊 ERD Diagram
The Entity Relationship Diagram below illustrates the database schema and relationships between tables.

![ERD Diagram](ERD/ERD_Auto_Insurance.png)

> 📁 The ERD file is located in the [`ERD/`](ERD) folder.

---

## 🖼 Screenshots
Here are some screenshots of the application in action:

| Main Dashboard | Customer Management |
|----------------|---------------------|
| ![Dashboard](screenshots/dashboard.png) | ![Customers](screenshots/customers.png) |

| Accident Recording | Claims Management |
|--------------------|-------------------|
| ![Accident](screenshots/accident.png) | ![Claims](screenshots/claims.png) |

> 📸 More screenshots can be found in the [`screenshots/`](screenshots) folder.

---

## ⚙️ Installation
### Prerequisites
- **Java JDK 17** or higher ([Download](https://www.oracle.com/java/technologies/javase-downloads.html))
- **JavaFX SDK 17** ([Download](https://gluonhq.com/products/javafx/))
- **MySQL Server 8.0** ([Download](https://dev.mysql.com/downloads/mysql/))
- **MySQL Workbench** (optional, for database management)
- **Git** (optional, for cloning)

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/layanbuirat/Insurance-system.git
   cd Insurance-system
   Set up the database

Open MySQL Workbench or your preferred MySQL client.

Create a new database:

sql
CREATE DATABASE insurance_db;
Import the provided SQL dump file:

bash
mysql -u root -p insurance_db < database/insurance_db.sql
Update the database credentials in the project configuration file (usually in src/db/DatabaseConfig.java).

Configure JavaFX

Download JavaFX SDK and extract it to a known location.

In your IDE, add the JavaFX library to the project and set VM options:

text
--module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml
🚀 How to Run
Using an IDE (IntelliJ IDEA / Eclipse)
Open the project in your IDE.

Ensure the JavaFX SDK is properly configured.

Run the main class: src/Main.java or the primary launcher class.

Using Command Line
Compile the project:

bash
javac --module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java
Run the application:

👥 Contributors
This project was developed by:

Name	Student ID	Role
Marah Mohammad	1211143	Developer
Leyan Burait	1211439	Maneger & Developer
Ahd Shereth	1211467	Developer
Tala Abadi	1210567	Developer

