## Project Status

🚧 Active Development

Mentora is currently under active development and continuously evolving.

### Current Focus Areas

* User Authentication & Authorization
* Role-Based Access Control (RBAC)
* Course & Content Management
* Assessment Engine
* Learning Analytics
* Recommendation System
* Activity Auditing
* Anonymous Community Discussions

---

## Core Database Tables

| Domain              | Tables                                   |
| ------------------- | ---------------------------------------- |
| Identity & Access   | organizations, users, roles, permissions |
| Learning Management | courses, modules, lessons, enrollments   |
| Assessment          | assessments, questions, submissions      |
| Community           | anonymous_posts                          |
| Intelligence        | recommendations                          |
| Auditing            | activity_logs                            |

---

## Sample API Endpoints

### Authentication

```http
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/auth/refresh-token
```

### Users

```http
GET /api/v1/users
GET /api/v1/users/{id}
PUT /api/v1/users/{id}
```

### Organizations

```http
POST /api/v1/organizations
GET /api/v1/organizations/{id}
```

### Courses

```http
POST /api/v1/courses
GET /api/v1/courses
GET /api/v1/courses/{id}
PUT /api/v1/courses/{id}
DELETE /api/v1/courses/{id}
```

### Modules

```http
POST /api/v1/modules
GET /api/v1/modules/{id}
```

### Lessons

```http
POST /api/v1/lessons
GET /api/v1/lessons/{id}
```

### Enrollments

```http
POST /api/v1/enrollments
GET /api/v1/enrollments
```

### Assessments

```http
POST /api/v1/assessments
GET /api/v1/assessments/{id}
```

### Submissions

```http
POST /api/v1/submissions
GET /api/v1/submissions/{id}
```

### Recommendations

```http
GET /api/v1/recommendations
```

### Anonymous Discussions

```http
POST /api/v1/posts
GET /api/v1/posts
GET /api/v1/posts/{id}
```

---

## Community & Collaboration

Mentora includes an anonymous discussion platform designed to encourage participation and knowledge sharing among learners.

### Features

* Anonymous posting
* Community engagement
* Peer-to-peer knowledge sharing
* Safe learning discussions
* Learner interaction and collaboration

---

## Security

Mentora implements enterprise-grade security using Spring Security.

### Security Features

* Authentication
* Authorization
* Role-Based Access Control (RBAC)
* Permission-Based Resource Access
* Method-Level Security
* Request Validation
* Secure Password Storage
* Multi-Tenant Access Controls

---

## Future Roadmap

### Phase 1

* JWT Authentication
* Refresh Token Support
* API Documentation with Swagger

### Phase 2

* Email Notifications
* Course Certificates
* User Profiles
* Progress Tracking Dashboard

### Phase 3

* AI-Powered Learning Recommendations
* Learning Path Generation
* Real-Time Analytics

### Phase 4

* Event-Driven Architecture
* Redis Caching
* Search Functionality
* External Integrations

---

## Why Modular Monolith?

Mentora adopts a Modular Monolith architecture to balance simplicity and scalability.

### Benefits

* Single deployment unit
* Clear domain boundaries
* Faster development cycle
* Easier debugging
* Lower operational complexity
* Future microservice extraction capability

Each module owns its business logic, domain entities, services, and APIs while remaining part of a unified application.


---


Author

Tosin John Okuwobi

Backend Engineer | Java & Spring Boot Developer

Focused on building scalable backend systems, fintech platforms, and learning technology solutions.
