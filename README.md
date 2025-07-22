# Student Attendance Management System (SAMS)

## Overview
This project is a Student Attendance Management System developed by **Vinodith Peiris** as part of an academic assignment. It helps manage students, lecturers, courses, class schedules, and attendance tracking with role-based login functionality (Admin & Lecturer).

The system follows a layered MVC architecture and uses MySQL for data storage.

---

## Setup Instructions

1. **Clone the repository:**

   ```bash
   git clone https://github.com/2005MeanudVino/Student-Attendance-Management-System.git
Import the project into your Java IDE (NetBeans, IntelliJ IDEA, Eclipse, etc.).

Setup MySQL Database:

Create a database named sams.

Import the database dump (sams_database.sql) using this command in your terminal/command prompt:

bash
Copy
Edit
mysql -u root -p sams < path/to/sams_database.sql

Configure database connection:

Edit the file src/edu/ijse/mvc/db/DBConnection.java to update your MySQL username, password, and host if different from defaults.

Run the project from your IDE.

Technologies Used
Java (Swing for UI)

MySQL (Database)

JDBC (Database connectivity)

Git (Version control)

Login Credentials
Role		Username	Password
Admin		admin		admin123
Lecturer	lect1		lect123

Developed by Vinodith Peiris