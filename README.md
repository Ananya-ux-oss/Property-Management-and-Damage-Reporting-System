# Property Management and Damage Reporting System

A single-file Java console application implementing the use-case diagram for
a university Property Management and Damage Reporting System, backed by
MySQL.

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
