# Simple Scheduler v1.0.1

## Features
- **Dashboard View**:
  - Reports on:
    - Appointment data by type, month, and type/month combination.
    - Each Contact's Appointment schedule.
    - Appointment workload for each User over time.
- **Customers View**:
  - Create, update, delete, and search Customers.
- **Appointments View**:
  - Filter, create, update, and delete Appointments.

---

## What's New
- **Simplified Setup**:
  - Migrated to Maven for easy dependency management.
  - Switched to H2 in-memory database for quick testing and showcasing.

---

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/simple-scheduler.git
   cd simple-scheduler
   ```

2. Build and run using Maven:
   ```bash
   mvn javafx:run
   ```

That's it! The application will run with an in-memory database, and no additional setup is required.

---

## Built Using
- Java SE 11+
- Maven
- JavaFX SDK 11.0.2
- H2 Database 2.1.214

---

## Last Update
- **Date**: 12/27/2024

---