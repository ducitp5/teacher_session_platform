# Business Analysis

## 1. Product Vision

### Project Name
**Teacher Session Platform**

### Goal
A platform where **teachers can publish teaching sessions (online or offline)** and **students can browse and enroll**.

### Target Users
- **Teachers** – create sessions
- **Students** – enroll in sessions
- **Admin** – moderate platform

### Main Value
- Teachers can **monetize knowledge**
- Students can **easily discover classes**
- Supports **both online and physical sessions**

---

# 2. Actors

## Teacher
Can:
- Register account
- Create sessions
- Manage sessions
- View enrolled students

## Student
Can:
- Register account
- Browse sessions
- Filter sessions
- Enroll in session
- Cancel enrollment

## Admin
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

# 4. Business Rules

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

# 5. Workflows

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
# 6. Extra Features (Good for AI Projects)

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
