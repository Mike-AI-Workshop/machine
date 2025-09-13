# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **machine equipment management system** (机器设备管理系统) for managing IT infrastructure including computer rooms, cabinets, devices, and interfaces. It's a production-ready full-stack application with version 1.0, completed with security checks.

## Architecture

### Frontend (machine-front)
- **Vue 3** with Composition API
- **Element Plus** for UI components
- **Pinia** for state management
- **Vite** for build tooling
- **Axios** for HTTP requests

### Backend (machine-backend)
- **Spring Boot 3.0.2** with Java 17
- **Spring Security** with JWT authentication
- **Spring Data JPA** + **MyBatis** for data access
- **MySQL** database
- **Maven** for project management

## Common Development Commands

### Frontend (machine-front/)
```bash
npm install          # Install dependencies
npm run dev          # Development server
npm run build        # Production build
npm run preview      # Preview production build
```

### Backend (machine-backend/)
```bash
mvn clean package    # Build JAR file
mvn test            # Run tests
java -jar target/machine-backend-0.0.1-SNAPSHOT.jar  # Run application
```

## Key Configuration Files

### Backend Configuration
- `machine-backend/src/main/resources/application.properties` - Database, JWT, and file upload settings
- `machine-backend/pom.xml` - Maven dependencies and build configuration
- Default MySQL database: `machine-backend`, username: `root`, password: `123456`
- JWT secret and expiration configured for production use
- File upload limit: 10MB, stored in `upload/` directory

### Frontend Configuration
- `machine-front/vite.config.js` - Vite configuration with API proxy to `http://localhost:8080`
- `machine-front/package.json` - Dependencies and build scripts
- Development server runs with proxy to backend at `/api`

## Database Schema

The system uses 9 core tables with cascading relationships:
- **users** - User management with role-based access (admin/user)
- **room** - Computer room management
- **cabinet_row** - Cabinet row organization
- **cabinet** - Individual cabinets
- **device** - Equipment within cabinets
- **interface** - Device interfaces
- **image_resource** - File upload management
- **marker** - Visual markers on images
- **system_content** - Dynamic content management

Database initialization files:
- `数据库表设计.sql` - Schema creation
- `home_content_init.sql` - Sample data (optional)

## Authentication & Security

- **JWT Token Authentication** - Stateless authentication with configurable expiration
- **Role-Based Access Control** - Admin and user roles with different permissions
- **Password Encryption** - BCrypt encryption for user passwords
- **CORS Configuration** - Supports frontend-backend separation
- **File Upload Security** - 10MB limit with proper path handling

## API Architecture

Backend follows RESTful API design with unified response format using `CommonResponse`. All API endpoints require JWT authentication except for:
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

## Frontend Architecture

- **Component-Based**: Reusable Vue components in `src/components/`
- **State Management**: Pinia stores for authentication and content
- **Routing**: Vue Router with lazy loading for views
- **API Integration**: Centralized API services in `src/api/`

## Development Guidelines

### Cursor Rules
The project includes Cursor development rules that emphasize:
- Cautious code modification practices
- Comprehensive code commenting
- Full-stack development expertise with Vue 3 + Spring Boot
- Chinese language responses

### Code Patterns
- Backend: Follows layered architecture (Controller → Service → Mapper → Entity)
- Frontend: Uses Vue 3 Composition API with proper component organization
- API responses use consistent `CommonResponse` format
- Error handling includes proper HTTP status codes and messages

## Environment Requirements

- **JDK 17+**
- **MySQL 5.7+**
- **Node.js 16+**
- **Maven 3.6+**

## Project Structure

```
machine/
├── machine-backend/          # Spring Boot backend
│   ├── src/main/java/com/example/machinebackend/
│   │   ├── config/          # Security, JWT, CORS config
│   │   ├── controller/      # REST API controllers
│   │   ├── entity/          # JPA entities
│   │   ├── service/         # Business logic
│   │   ├── mapper/          # MyBatis data access
│   │   └── utils/           # Utility classes
│   └── src/main/resources/
├── machine-front/           # Vue 3 frontend
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── views/          # Page components
│   │   ├── store/          # Pinia state management
│   │   ├── router/         # Vue Router config
│   │   └── api/            # API services
└── .cursor/rules/          # Development guidelines
```

## Important Notes

- This is a production-ready system with completed security checks
- Always maintain the separation between frontend and backend concerns
- Use the existing authentication and authorization patterns
- Follow the established naming conventions and file organization
- The system supports image uploads and file management with proper security measures