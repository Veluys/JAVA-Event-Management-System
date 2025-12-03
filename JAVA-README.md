# Plan-ET: Java Console-Based School Event Management System

## Project Overview
<p align="justify"><b>Plan-ET</b> is a Java console-based system designed for BSU Malvar Campus to manage school events.
It supports full CRUD operations for events stored in a PostgreSQL database, participant registration, 
and attendance tracking.</p>

## Objectives
- Enable school event organizers to plan and manage school events
- Allow organizers to easily add, view, search, and remove event participants
- Provide attendance tracking to monitor attendees and absentees

---

## Features Implemented

### Minimum Features
- Add/edit/delete events
- Register participants
- View event and participant lists

### Twisted Features
- **Conflict detector**: Warns if two events overlap in datetime
- **Attendance tracking**: Mark participants as present or absent

---

## How to Use

### 1. System Start
1. Run `Main.java` using a Java compiler
2. Choose from main menu: **1 - Events**, **2 - Participants**, **3 - Attendance**, **4 - Exit**
3. Use **Exit** option in sub-menus to return to main menu

### 2. Events Menu (Option 1)
1. **Add**: Enter event name, date, start time, end time, venue (select from campus list)
2. **View**: Completed, Scheduled, Upcoming, Ongoing
3. **Search**: By event name
4. **Update**: Enter name, modify attributes (press Enter to skip)
5. **Delete**: Enter event name

### 3. Participants Menu (Option 2)
1. Select event name
2. **Add**: Enter student SR Code
3. **View All**, **Search**, **Remove** by SR Code

### 4. Attendance Menu (Option 3)
1. Select event name
2. **View Attendees/Absentees**, **Search** by SR Code
3. **Set Present/Reset Absent** by SR Code

---

## System Architecture (MVC Pattern)
- **MVC**: Separates database access, user input, and display logic

---

## Key Classes & Methods

### Main.java
- `main()`: Entry point
- `getConnection()`, `closeConnection()`: Database handling

### Controllers
| Class | Key Methods |
|-------|-------------|
| **EventController** | `execute()`, `addEvent()`, `viewEvents()`, `searchEvent()`, `updateEvent()`, `deleteEvent()` |
| **RegController** | `addRegistration()`, `viewRegistered()`, `searchRegistered()`, `removeRegistered()` |
| **AttendanceController** | `viewAttendees()`, `viewAbsentees()`, `markPresent()`, `markAbsent()` |

### DAOs
| DAO | Key Methods |
|-----|-------------|
| **EventDAO** | `insert()`, `showEvents()`, `eventsInConflict()`, `update()`, `delete()` |
| **RegistrationDAO** | `insert()`, `show()`, `search()`, `delete()` |
| **AttendanceDAO** | `showAttendees()`, `markPresent()`, `checkAttendanceStatus()` |

---

## Challenges & Solutions
| Challenge | Solution |
|-----------|----------|
| Feature scope | Limited to school events |
| Project structure | MVC architecture |
| Database design | Entity Relationship Diagram |
| Date/time validation | Java built-in date/time objects |
| Consistent output | Unified Displayer methods |

---

## Learnings
- Early planning prevents issues along project timeline
- Use familiar tools consistently
- Version control with feature branches for refactoring
- Search existing solutions first
- Read documentation progressively
- Defensive programming for unusual user inputs

---

## References
- **Java**: [Oracle Docs](https://docs.oracle.com/javase/8/index.html), [GeeksForGeeks](https://www.geeksforgeeks.org), [W3Schools](https://www.w3schools.com), [Programiz](https://www.programiz.com/java-programming)
- **Database**: [PostgreSQL Docs](https://www.postgresql.org/docs/current/index.html), [DataCamp](https://www.datacamp.com)
- **Version Control**: [Git](https://git-scm.com/docs/git), [GitHub](https://github.com)
- **Tools**: [Lucid](https://lucid.app) (planning), [Stack Overflow](https://stackoverflow.com)(debugging)
