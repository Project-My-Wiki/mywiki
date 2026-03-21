# Project Context
This project is a backend REST API for a knowledge-sharing wiki service.
Focus on clean, maintainable, and scalable code.

# Tech Rails
- Language: Kotlin
- Framework: Spring Boot 3.5
- Database: JPA (Hibernate), MySQL 8.0
- Build Tool: Gradle

# Vibe & Coding Rules
- Ensure declarative and functional Kotlin style where appropriate.
- Ensure all API responses follow a standard global JSON wrapper format.
- Use constructor injection for all Spring components.
- Keep controllers lean; business logic must reside in the service layer.
- 반드시 한국어로 대답할 것.

# Testing
- Ensure JUnit 5 and MockK are used for all unit tests.
- Maintain a Red-Green-Refactor approach for new features.

# Commit message format
- 반드시 한국어로 작성할 것. 아래의 형식을 따라야 함.
    - feat: new feature
    - fix: bug fix
    - docs: documentation
    - style: formatting (white-space, missing semi-colons, etc)
    - refactor: refactoring (improving code structure)
    - test: adding missing tests or correcting existing tests
    - chore: maintenance tasks