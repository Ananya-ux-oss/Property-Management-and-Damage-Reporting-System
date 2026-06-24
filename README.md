# Property Management and Damage Reporting System

A Java console application that is intended to be used by a small sized college like BITS College where the manager(primary user) registers properties, assign location to registered properties and review reports made by students on damaged assets while the students report damage done to properties. The backed by
MySQL database that is used to store the users(for authentication), properties and damage reports. 

If the program is to further develope, it is envisioned that it would become a web-based or desktop application where students can submit reports using images while the manager has a fully interactive map of the assets of the university.

## What it does

The program models three actors from the use-case diagram and lets you
drive all of their actions interactively from the keyboard:

- **Student** — logs in, reports property damage
- **Manager** — logs in, registers properties, assigns locations, summarizes
  property information, reviews submitted damage reports
- **Admin** — logs in, registers new Manager and Student accounts

On startup the program seeds one demo account for each role so there's
always something to log in with, then drops into an interactive menu where
you can use those accounts, or have the Admin create more.

## Project structure

```
PropertyManagementSystem/
├── PropertyManagementSystem.java
├── db/
│   ├── schema.sql
│   └── Database.java
├── interfaces/
│   ├── Reviewable.java
│   └── Summarizable.java
├── exception/
│   ├── AuthenticationException.java
│   ├── DatabaseException.java
│   ├── PropertyNotFoundException.java
│   ├── SystemException.java
│   └── UnauthorizedException.java
├── ui/
│    └── InputHandler.java
├── model/
│   ├── DamageReport.java
│   ├── Property.java
│   ├── user.java
│   ├── Admin.java
│   ├── Student.java
│   └── Manager.java
├── lib/
│    └── 
└── ui/
    └── InputHandler.java

```
Everything — model classes, exceptions, database access, file logging, and
the interactive menus — lives in `PropertyManagementSystem.java`, organized
into clearly commented sections:

## Java concepts demonstrated

| Concept | Where |
|---|---|
| Abstract classes & inheritance | `User` → `Admin`, `Manager`, `Student` |
| Interfaces | `Reviewable`, `Summarizable` |
| Polymorphism | `getRole()` overridden per subclass |
| Custom checked exceptions | `SystemException` hierarchy, used throughout |
| File handling | `FileLogger` (`BufferedWriter`/`BufferedReader`, try-with-resources) |
| JDBC / MySQL database | `DatabaseConnection`, `Database` |
| Collections | `List`, `Map`, `ArrayList`, `LinkedHashMap`, `HashMap` |
| Enums | `DamageReport.Status` (`PENDING` → `REVIEWED` → `RESOLVED`) |
| Singleton pattern | `DatabaseConnection.getInstance()`, `Database.getInstance()` |
| User input | `Scanner` throughout `InputHandler` |

## Database Setup
**No manual setup is required.** 
The program only needs a MySQL server to be functional and running on port 3306.
The moment the program runs, `DatabaseConnection.java` will automatically:

1. Connect to MySQL Sever.
2. Run the command `CREATE DATABASE IF NOT EXISTS property_management_db`.
3. Run `CREATE TABLE IF NOT EXISTS` for `users`, `properties`, and `damage_reports`.
4. Reconnect to the database if the program is run for the second time.

`DatabaseConnection.java` uses the default settings for MySQL for username(`root`), password(none) and port number(`localhost:3306`).

If MySQL server is not available, the programs returns `Database connection failed. App cannot run.` message and the program will terminate.

The schema for the database stored at `db/schema.sql` serves as a reference template for the database logical design.

## MySQL JDBC driver

The program automatically provides a MySQL driver for Java in lib package. The driver version is mysql-connector-j-7.9.0.jar and the user is not required to download the driver from MySQL website.

## Compile and Run

```For Linux
javac PropertyManagementSystem.java
java -cp '.:mysql-connector-j-7.9.0.jar' PropertyManagementSystem
```

```For Windows
javac PropertyManagementSystem.java
java -cp '.;mysql-connector-j-7.9.0.jar' PropertyManagementSystem
```
## How it runs
The program comes with an one Admin user already registered and the account of the admin (email and password) fully revealed. Then using the interactive menu, the user can register students and managers. Then the user can use functionalities of the both students and managers by logging into the accounts. 

**Phase 2 — Interactive menu.** A main menu then lets you choose:

```
[1] Student Portal
[2] Manager Portal
[3] Admin Portal
[4] Exit
```

Typing the number of choice leads to subsequent portal and under each portals, there are several interactive options as well.

## Note
-`activity_log.txt` is created or appended to in the working directory everytime the program runs and logs activities done in while the program runs along with time stamps.
