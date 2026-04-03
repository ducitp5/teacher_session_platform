# Teacher Session Platform - User Guide

Welcome to the Teacher Session Platform! This guide explains how to use the application based on your role: **Student** or **Teacher**.

## 1. Getting Started

### Registration
To use the platform, you must first create an account:
1. Navigate to the **Register** page (`/register`).
2. Fill in your details (First Name, Last Name, Email, Password).
3. Select your role: **Student** or **Teacher**.
4. Submit the form. You will be redirected to the Login page upon success.

### Login
1. Navigate to the **Login** page (`/login`).
2. Enter your email and password.
3. Upon logging in, you will be redirected to your respective dashboard or the home page depending on your role.

---

## 2. Student Guide

As a Student, you can browse available learning sessions and enroll in them.

### Browsing Sessions
- **Home Page (`/`)**: Displays a list of all available sessions.
- **Session Details (`/sessions/{id}`)**: Click on any session to view its details (description, date, time, and teacher).

### Enrolling in a Session
1. From the Home Page or Session Details page, find a session you wish to join.
2. Click the **Enroll** button.
3. Upon success, you will be redirected to your dashboard with a success message.

### Managing Enrollments (Student Dashboard)
- Navigate to **My Dashboard** (`/student/dashboard`).
- Here, you will see a list of all your active enrollments.
- To cancel an enrollment, click the **Cancel Enrollment** button next to the desired session.

---

## 3. Teacher Guide

As a Teacher, you can create and manage your teaching sessions.

### Teacher Dashboard
- Navigate to **My Dashboard** (`/teacher/dashboard`).
- This page displays all the sessions you have created.
- You can monitor your sessions and see their details here.

### Creating a New Session
1. From your dashboard, click on **Create New Session** (or navigate to `/teacher/sessions/create`).
2. Fill out the session details (Title, Description, Date, Time, Capacity, etc.).
3. Submit the form. The new session will now be visible to students on the Home Page.

### Canceling a Session
1. Go to your **Teacher Dashboard**.
2. Find the session you wish to cancel.
3. Click the **Cancel Session** button.
4. The session will be removed or marked as canceled, and it will update the dashboard accordingly.

---

## 4. Logging Out
To securely end your session, click the **Logout** link in the navigation menu. This will sign you out and return you to the Login page.
