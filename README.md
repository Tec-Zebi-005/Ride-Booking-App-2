# RideFlow — Ride Booking App

RideFlow is a desktop ride-booking application built with **Java Swing** and **SQL Server**. It lets passengers book rides and drivers accept them — all through a clean, easy-to-use interface.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Database Setup](#database-setup)
- [How to Run](#how-to-run)
- [How It Works](#how-it-works)
- [Screenshots Overview](#screenshots-overview)
- [Notes](#notes)

---

##  Features

### For Passengers
- Sign up and log in as a passenger
- Book a ride by entering pickup and dropoff locations
- Choose vehicle type (Bike, Car, AC Car, Premium)
- View real-time ride status (Pending → Accepted → Completed)
- Pay for rides through a secure payment page (Cash, EasyPaisa, JazzCash, Credit/Debit Card)
- Leave feedback and star ratings for drivers
- View full ride history

### For Drivers
- Sign up and log in as a driver
- Select vehicle type during registration
- View available ride requests matching their vehicle type
- Accept and complete rides
- Dashboard showing current ride status

### Admin
- Admin panel to oversee app data (`RideAdminApp.java`)

---

##  Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (JDK 8+) |
| UI Framework | Java Swing |
| Database | Microsoft SQL Server (Express) |
| DB Driver | JDBC (Microsoft SQL Server JDBC) |
| IDE (recommended) | IntelliJ IDEA / Eclipse |

##  Database Setup

1. Open **SQL Server Management Studio (SSMS)**
2. Open the file `DBMS_Query.txt`
3. Run the entire script — it will:
   - Create the `RideFlowDB` database
   - Create all required tables: `Users`, `Passengers`, `Drivers`, `ride_requests`, `feedback`
   - Create helpful views: `PassengerDetails`, `DriverDetails`

### Tables Overview

| Table | Purpose |
|---|---|
| `Users` | Stores all users (passengers & drivers) |
| `Passengers` | Passenger-specific info |
| `Drivers` | Driver info including license & vehicle type |
| `ride_requests` | All ride bookings and their statuses |
| `feedback` | Post-ride ratings and comments |

---

## ▶️ How to Run

### Prerequisites
- Java JDK 8 or higher installed
- Microsoft SQL Server Express installed and running
- Microsoft JDBC Driver for SQL Server added to your project classpath

### Steps

1. **Clone or download** this repository
2. **Set up the database** using `DBMS_Query.txt` (see above)
3. **Check the DB connection** in `DatabaseConnection.java`:
   ```
   Server: localhost\SQLEXPRESS
   Port: 1433
   Database: RideFlowDB
   Authentication: Windows Integrated Security
   ```
4. **Add the JDBC driver** (`mssql-jdbc-xx.jar`) to your project's build path
5. **Run `Main.java`** first to confirm the database connects successfully
6. **Run `LoginPage.java`** to launch the app

---

##  How It Works

```
Passenger signs up → Books a ride → Ride shows as PENDING
         ↓
Driver logs in → Sees PENDING rides → Accepts a ride → Ride becomes ACCEPTED
         ↓
Passenger sees ride accepted → Goes to Payment Page → Pays → Ride becomes COMPLETED
         ↓
Passenger is redirected to Feedback Page → Submits rating & comments → Back to Dashboard
```

### Ride Status Flow

| Status | Meaning |
|---|---|
| `PENDING` | Ride booked, waiting for a driver |
| `ACCEPTED` | Driver has accepted the ride |
| `COMPLETED` | Payment done, ride finished |

---

## 💡 Notes

- Passwords are currently stored as plain text. For production use, replace with hashed passwords (e.g., BCrypt).
- The payment page simulates fare calculation based on vehicle type, distance, and time — no real payment gateway is connected.
- If the database is unavailable, feedback falls back to saving in a local `feedbacks.txt` file.
- The app uses **Windows Integrated Security** for SQL Server — no username/password needed for the DB connection.

---

## 👥 Roles

| Role | Access |
|---|---|
| Passenger | Book rides, pay, leave feedback, view history |
| Driver | Accept & complete rides, view assigned trips |
| Admin | View all app data via admin panel |

---

*Built with Java Swing & SQL Server — RideFlow makes campus or city rides simple.*
