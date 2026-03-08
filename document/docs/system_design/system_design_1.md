# System Design: Teacher Session Platform

## 1. Architecture Overview
The platform uses a microservices-inspired architecture with separate backend and frontend projects.
- **Backend (Initial Fullstack)**: Spring Boot 3 using Clean Architecture (Controller, Service, Repository, DTO, Mapper, Entity) and Thymeleaf for serving initial views.
- **Frontend (Later Phase)**: React.js with TailwindCSS (to be built later at `/frontend/teacher-session-ui`).
- **Database**: MySQL database hosted locally.

## 2. Core Domain Models & Database Schema
The database will be structured as follows:

### Users
- `id` (PK)
- `email` (Unique)
- `password`
- `first_name`
- `last_name`
- `role` (STUDENT, TEACHER, ADMIN)
- `created_at`
- `updated_at`

### Teacher Profiles
- `id` (PK)
- `user_id` (FK to Users)
- `bio`
- `expertise_area`
- `rating_average`

### Sessions
- `id` (PK)
- `teacher_id` (FK to Users)
- `title`
- `description`
- `subject`
- `price` (Decimal)
- `max_students` (Int)
- `enrolled_students` (Int, default 0)
- `session_type` (Enum: ONLINE, OFFLINE)
- `location` (Nullable, for offline)
- `meeting_link` (Nullable, for online)
- `start_date` (DateTime)
- `duration_minutes` (Int)
- `status` (Enum: OPEN, FULL, CANCELLED, COMPLETED)
- `created_at`
- `updated_at`

### Enrollments
- `id` (PK)
- `session_id` (FK to Sessions)
- `student_id` (FK to Users)
- `status` (Enum: ACTIVE, CANCELLED)
- `enrolled_at`

## 3. API Endpoints

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Users
- `GET /api/users/me`
- `GET /api/teachers/{id}/profile`

### Sessions
- `GET /api/sessions` (with filters: subject, teacher, online/offline)
- `GET /api/sessions/{id}`
- `POST /api/sessions` (Teacher only)
- `PUT /api/sessions/{id}` (Teacher only)
- `DELETE /api/sessions/{id}` (Teacher only, before start_date)
- `GET /api/sessions/teacher/me` (Teacher only)

### Enrollments
- `POST /api/sessions/{id}/enroll` (Student only, checks session status & capacity)
- `POST /api/enrollments/{id}/cancel` (Student only)
- `GET /api/enrollments/student/me` (Student only)
- `GET /api/sessions/{id}/students` (Teacher only)

## 4. Workflows & Rules
1. **Enrollment**: When a student enrolls, `enrolled_students` is incremented. If `enrolled_students == max_students`, the session `status` updates to `FULL`.
2. **Cancellation**: Teachers can cancel if `start_date` is in the future.
3. **Session Type Validations**: If `session_type == ONLINE`, `meeting_link` is required. If `OFFLINE`, `location` is required.

## 5. Technology Choices
- **Backend Framework**: Spring Boot 3 with Spring Web, Spring Data JPA, Spring Security (JWT authentication), Lombok.
- **Frontend Framework**: React 18, React Router DOM, Axios, TailwindCSS.
- **Database**: MySQL.
- **Deployment**: Docker Compose for orchestrating backend and frontend services.
