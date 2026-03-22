### Workspace Structure & Microservices Architecture
Root folders:

/backend
/frontend
/document

- **Backend Directory (`/backend`)**: Always create a `backend` folder at the workspace root. Inside this folder, create separated projects as microservices using backend frameworks (e.g., Laravel, Spring Boot using full Thymeleaf, Django, etc.).
  Each backend microservice (if spring boot) must follow:

service-name/
├── src
├── config
├── controllers
├── services
├── repositories
├── entities
├── dto
├── mappers
├── tests

- **Frontend Directory (`/frontend`)**: Always create a `frontend` folder at the workspace root. Inside this folder, create separated projects as microservices using frontend frameworks (e.g., Vue.js, React.js, etc.).

- **Root Configuration**: Always include `.env` and `.gitignore` files at the workspace root.

- **Dockerization**: Set up Docker for all microservices inside both the `backend` and `frontend` folders. Orchestrate these services using a `docker-compose.yml` file at the root.

- **Database Servers**: Use localhost database servers (e.g., MySQL, MongoDB, etc.) rather than containerized databases unless specified.

-Number of parameters in a method > 3, put in an array.
-Database structure is located in document/db/db_structure.sql.
-File upload into document/upload

- when use a class inside the code, use import class, dont use the full package class name. ex : dont use  java.util.List<Room> rooms, use import java.util.List

### Testing Rules

Each backend service must include:

Unit tests
Service tests
Controller tests

Test directory:

src/test/
