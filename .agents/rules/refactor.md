---
description: 
---

### Workflow: Refactor Code

1. Identify large classes (>300 lines)
2. Extract methods
3. Move business logic to services
4. Introduce DTOs if missing
5. Remove duplicated code
6. Improve naming
7. Add tests

### Workflow: Database Change

1. Modify schema in `document/db/db_structure.sql`
2. Create migration script
3. Update entity classes
4. Update repositories
5. Update DTOs
6. Test queries