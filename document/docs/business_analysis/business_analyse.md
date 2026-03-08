# 1. Product Vision (for Antigravity)

## Project Name
**Teacher Session Platform**

## Goal
A platform where **teachers can publish teaching sessions (online or offline)** and **students can browse and enroll**.

## Target Users
- **Teachers** – create sessions
- **Students** – enroll in sessions
- **Admin** – moderate platform

## Main Value
- Teachers can **monetize knowledge**
- Students can **easily discover classes**
- Supports **both online and physical sessions**

---

# 2. Business Analysis Document (BAD)

## Actors

### Teacher
Can:
- Register account
- Create sessions
- Manage sessions
- View enrolled students

### Student
Can:
- Register account
- Browse sessions
- Filter sessions
- Enroll in session
- Cancel enrollment

### Admin
Can:
- Manage users
- Moderate sessions
- Suspend teachers
- Manage reports

---

# 3. Core Features

## Authentication

Users can register as:
- **Student**
- **Teacher**

Authentication methods:
- Email / Password
- OAuth (optional)

---

## Teacher Features

Teacher can:

### Create Session
Fields:
- `title`
- `description`
- `subject`
- `price`
- `maxStudents`
- `sessionType` (online / offline)
- `location` (if offline)
- `meetingLink` (if online)
- `startDate`
- `duration`

### Manage Sessions
- Edit session
- Cancel session
- View enrolled students

---

## Student Features

Student can:

### Browse Sessions
Search filters:
- `subject`
- `teacher`
- `price`
- `online / offline`
- `date`
- `location`

### Enroll
Steps:
1. Click **Enroll**
2. Confirm enrollment

---

## Session Types

### Online Session

Fields:
- `meetingLink`
- `platform` (Zoom / Google Meet)

### Offline Session

Fields:
- `address`
- `city`
- `mapLocation`

# 4. Domain Model (Very Important for AI)

## User

```
User
- id
- name
- email
- password
- role (STUDENT, TEACHER, ADMIN)
- createdAt
```

## TeacherProfile

```
TeacherProfile
- id
- userId
- bio
- expertise
- rating
```

## Session

```
Session
- id
- teacherId
- title
- description
- subject
- price
- sessionType (ONLINE / OFFLINE)
- location
- meetingLink
- maxStudents
- startTime
- durationMinutes
- status (OPEN / FULL / CANCELLED)
```

## Enrollment

```
Enrollment
- id
- sessionId
- studentId
- status (ENROLLED / CANCELLED)
- enrolledAt
```

---

# 5. Database Schema (MySQL)

Example tables.

## users

```
id
name
email
password
role
created_at
```

## teacher_profiles

```
id
user_id
bio
expertise
rating
```

## sessions

```
id
teacher_id
title
description
subject
price
session_type
location
meeting_link
max_students
start_time
duration_minutes
status
```

## enrollments

```
id
session_id
student_id
status
created_at
```

---

# 6. System Architecture

Recommended architecture for your stack.

### Tech Stack

* **Spring Boot**
* **React**
* **Thymeleaf**
* **MySQL**
* **MongoDB**

### Architecture

```
Frontend
  React + Tailwind

Backend
  Spring Boot API

Database
  MySQL (core data)
  MongoDB (logs / analytics)

Services
  Auth Service
  Session Service
  Enrollment Service
```

---

# 7. Business Rules

Important for AI generation.

## Session Creation Rules

Teacher can create session only if:

```
user.role == TEACHER
```

## Enrollment Rules

Student can enroll only if:

```
session.status == OPEN
AND
enrolledStudents < maxStudents
```

## Session Full Rule

```
if enrolledStudents >= maxStudents
    session.status = FULL
```

## Cancellation Rules

Teacher can cancel session only:

```
before session.startTime
```

---

# 8. Workflows

## Teacher Creates Session

```
Teacher login
     ↓
Click "Create Session"
     ↓
Fill form
     ↓
Save session
     ↓
Session status = OPEN
```

## Student Enrolls

```
Student login
     ↓
Browse sessions
     ↓
Click session
     ↓
Click enroll
     ↓
Create Enrollment
```

## Session Becomes Full

```
Enrollment created
     ↓
Count students
     ↓
If max reached
     ↓
Session status = FULL
```

# 9. Extra Features (Good for AI Projects)

Add these later.

---

## Reviews

Students can rate teachers after attending a session.

Possible fields:
- `rating`
- `comment`
- `studentId`
- `teacherId`
- `sessionId`
- `createdAt`

---

## Payment

Integrate payment gateways.

Examples:
- **Stripe**
- **VNPay**

Possible features:
- Pay when enrolling
- Refund if session cancelled
- Payment history

---

## Notifications

Send notifications to users.

### Email notifications when:
- Student **enrolls** in a session
- Teacher **cancels** a session
- Session **reminder before start**

---

## Chat

Real-time communication between users.

Features:
- **Student ↔ Teacher chat**
- Messaging inside session page
- Optional real-time system (WebSocket)