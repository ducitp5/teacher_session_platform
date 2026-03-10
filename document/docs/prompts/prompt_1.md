# AI Project Generator Prompt

## Role

You are a **senior software architect and full-stack engineer**.

Read the file **`business_analyse_1.md`**, then generate the **system design** and save it into: system_design_1.md

---

# Project Description

Generate a **full project** for a **teaching platform** where:

- Teachers can create teaching sessions
- Students can browse sessions
- Students can enroll in sessions

---

# Tech Stack

## Backend

- Spring Boot 3
- Thymeleaf  
  *(first phase uses Thymeleaf only for developers)*
- JPA / Hibernate
- MySQL
- REST API

## Frontend

- React
- TailwindCSS

---

# Architecture

Use **Clean Architecture** with the following layers:

- Controller
- Service
- Repository
- DTO
- Mapper

---

# Core Domain

The platform must include these main domains:

- Users
- Sessions
- Enrollments
- Teacher Profiles

---

# Business Rules

- Teachers can **create sessions**
- Students can **browse sessions**
- Students can **enroll in sessions**
- Sessions can be:
    - `ONLINE`
    - `OFFLINE`

---

# What to Generate

Generate the following components:

1. Database schema
2. JPA entities
3. Repositories
4. Services
5. Controllers
6. DTOs
7. Validation rules
8. React pages
9. React API services
10. Tailwind UI components

---

# Coding Standards

Use **best practices** for:

- Spring Boot
- React
- Clean architecture
- Maintainable code structure

---

# Project Structure
/backend
/frontend


---

# Generation Strategy

Generate the project **step by step**.