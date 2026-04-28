---
description: Project Coding Rules & Conventions
---

# Coding Conventions Workflow

When writing or refactoring code within this project, you must strictly follow the rules and conventions defined in this workflow. This ensures a clean, maintainable, and uniform codebase across all microservices.

---

## 🏗️ 1. Object Encapsulation over Data Fragmentation

When transferring data across application boundaries (e.g., Services, Controllers, Sessions, and Views), always prefer passing complete objects (e.g., DTOs or Entities) rather than breaking them down into individual primitive variables. This logic ensures the code remains short, clean, and strictly conforms to Object-Oriented design patterns.

### 🚫 Rule: Do NOT pass granular or fragmented properties
Avoid unpacking an object just to store its individual properties in a Model, Session, or method argument. This bloats the code and increases the risk of state inconsistencies.

**Incorrect Example:**
```java
// Controller
session.setAttribute("userId", user.getId());
session.setAttribute("userRole", user.getRole().name());
session.setAttribute("userFirstName", user.getFirstName());

model.addAttribute("courseTitle", course.getTitle());
model.addAttribute("coursePrice", course.getPrice());
```

### ✅ Rule: DO pass the complete object directly
Transfer the parent object itself. This grants the receiver (e.g., Thymeleaf templates, inner services) immediate access to all data fields in a logical structure, eliminating redundant assignments.

**Correct Example:**
```java
// Controller
session.setAttribute("userDto", userDto);
model.addAttribute("courseSession", courseSessionDto);
```

### 💻 Correct Usage in Views (Thymeleaf)
Rely on the object's properties directly inside templates instead of reading single-purpose variables.

**HTML Example:**
```html
<!-- Incorrect: Relying on fragmented, individual attributes -->
<span th:text="${userFirstName}">User Name</span>
<p th:text="${courseTitle} + ' costs ' + ${coursePrice}">Details</p>

<!-- Correct: Calling the complete object's internal properties -->
<span th:text="${userDto.firstName}">User Name</span>
<p th:text="${courseSession.title} + ' costs ' + ${courseSession.price}">Details</p>
```

### Benefits of Object Encapsulation
- **Shorter Code:** Eliminates repetitive `setAttribute` or `addAttribute` boilerplate.
- **Improved Maintainability:** Adding a new property (e.g., `user.lastName`) doesn't require updating controller signatures and model assignments. The view can access it directly from the existing object.
- **Clean Architecture:** Ensures the UI strictly binds to the established Data Transfer Objects (DTO) schema instead of ad-hoc keys.

---

## 🚨 2. Exception Handling Logic

This section outlines the standard pattern for handling and raising exceptions across the backend microservices.

### Domain-Specific Exceptions
Every functional domain or major service (e.g., `Auth`, `Session`, `Enrollment`) **must** define its own dedicated custom runtime exception. 
- Avoid throwing generic exceptions like `IllegalArgumentException`, `RuntimeException`, or general `ResourceNotFoundException`.
- **Examples**: `AuthException`, `SessionException`, `EnrollmentException`.

### Dedicated Error Code Enums
Every custom domain exception must be paired with its own `ErrorCode` enum. This enum serves as a centralized dictionary for all possible error states that can occur within that domain.
- **Location**: Enums must be placed in the `com.teachersession.exceptions.enums` package.
- **Structure**: Each enum constant must encompass a descriptive, human-readable message.
- **Examples**: `AuthErrorCode`, `SessionErrorCode`, `EnrollmentErrorCode`.

### No Hardcoded Strings
Hardcoded error messages within `.orElseThrow()` or `throw new ...` statements are **strictly forbidden** in business logic.
- **Incorrect**: `throw new IllegalArgumentException("Session not found");`
- **Correct**: `throw new SessionException(SessionErrorCode.SESSION_NOT_FOUND);`

### Enum Implementation Pattern (with Lombok)
An ErrorCode enum should encapsulate the string message representing the error. Always use Lombok's `@Getter` to avoid writing manual getter methods.

```java
package com.teachersession.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExampleErrorCode {
    RESOURCE_NOT_FOUND("The requested resource could not be found"),
    VALIDATION_FAILED("The provided input is invalid");

    private final String message;

    ExampleErrorCode(String message) {
        this.message = message;
    }
}
```

### Exception Implementation Pattern
The custom exception should extend `RuntimeException` and accept the corresponding `ErrorCode` enum in its constructor, passing the enum's message up to the `super()` class constructor.

```java
package com.teachersession.exceptions;

import com.teachersession.exceptions.enums.ExampleErrorCode;
import lombok.Getter;

@Getter
public class ExampleException extends RuntimeException {

    private final ExampleErrorCode errorCode;

    public ExampleException(ExampleErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```

### Benefits of Custom Exceptions
- **Uniformity:** Error responses and logs are completely standardized.
- **Centralization:** Error messages are traceable and easily translatable/modifiable from a single location.
- **Dry Code:** Eliminates repetitive string message definitions across different service methods.

---

## 🚀 3. Utilizing Lombok & Code-Minimizing Annotations

To keep the codebase as clean, short, and readable as possible, **always** use Lombok and relevant framework annotations instead of writing boilerplate code manually.

### 🚫 Rule: Do NOT write boilerplate code manually
Never manually generate Getters, Setters, default Constructors, or Builder patterns.

**Incorrect Example:**
```java
public class UserDto {
    private String email;

    public UserDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

### ✅ Rule: DO use appropriate Annotations
Use Lombok annotations (`@Data`, `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor`) and corresponding Spring annotations to let the compiler generate the boilerplate.

**Correct Example:**
```java
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
}
```

### Dependency Injection
When injecting dependencies into a Spring `@Service` or `@Controller`, use `@RequiredArgsConstructor` alongside `private final` fields instead of writing `autowired` constructors.

**Correct Example:**
```java
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository; // Auto-injected by Lombok!
}
```

---

## 📏 4. Method Parameter Limits (Associative Array / Map)

When a method or function requires more than 3 parameters, they must be bundled into an associative array (e.g., a `Map<String, Object>` in Java, which behaves similarly to PHP's associative arrays). This reduces method signature bloat and keeps the code readable.

### 🚫 Rule: Do NOT pass more than 3 parameters individually
Long parameter lists make method signatures hard to read and increase the chance of parameter ordering mistakes.

**Incorrect Example:**
```java
public void processSearch(String query, String category, boolean isActive, String sortBy, int limit) {
    // Too many individual parameters!
}
```

### ✅ Rule: DO group > 3 parameters into an associative Map
Pass a single `Map` containing the necessary key-value pairs. (Alternatively, a dedicated single object transfer like a DTO is also acceptable as per Section 1).

**Correct Example:**
```java
public void processSearch(Map<String, Object> params) {
    String query = (String) params.get("query");
    String category = (String) params.get("category");
    boolean isActive = (Boolean) params.get("isActive");
    // Resume processing...
}
```

---

## 📦 5. Explicit Imports Over Fully Qualified Names

Always use `import` statements at the top of your Java files rather than using fully qualified package class names directly inside your code logic or variables. This keeps the code clean and easier to read.

### 🚫 Rule: Do NOT type out the full package path in the code body
Never declare variables, method arguments, or perform `instanceof` checks using the entire package path.

**Incorrect Example:**
```java
// Inside a class
private java.util.List<com.teachersession.entities.Room> rooms;

if (exception instanceof org.springframework.dao.DataAccessException) {
    // ...
}
```

### ✅ Rule: DO use import statements
Use standard `import` statements at the top of the file and reference only the local class name within the code.

**Correct Example:**
```java
import java.util.List;
import com.teachersession.entities.Room;
import org.springframework.dao.DataAccessException;

// Inside a class
private List<Room> rooms;

if (exception instanceof DataAccessException) {
    // ...
}
```
